package com.tredbase.payment.service;

import com.tredbase.payment.dto.request.PaymentDto;
import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.PaymentResponse;
import com.tredbase.payment.dto.response.ResponseCodes;
import com.tredbase.payment.entities.Parent;
import com.tredbase.payment.entities.Payment;
import com.tredbase.payment.entities.Student;
import com.tredbase.payment.entities.enums.PaymentStatus;
import com.tredbase.payment.entities.enums.PaymentType;
import com.tredbase.payment.exception.InsufficientBalanceException;
import com.tredbase.payment.exception.InvalidPaymentTypeException;
import com.tredbase.payment.exception.InvalidUserIdException;
import com.tredbase.payment.exception.ParentNotFoundException;
import com.tredbase.payment.exception.StudentNotFoundException;
import com.tredbase.payment.repository.ParentRepository;
import com.tredbase.payment.repository.PaymentRepository;
import com.tredbase.payment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.tredbase.payment.utils.GeneralConstants.PROCESSING_FEE_DEFAULT_RATE;
import static com.tredbase.payment.utils.GeneralConstants.PROCESSING_FEE_DISCOUNTED_RATE;
import static com.tredbase.payment.utils.ResponseConstants.SUCCESS_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public BaseResponse<PaymentResponse> processPayment(PaymentDto dto) {
        log.info("Processing payment for Student ID: {}, Parent ID: {}", dto.getStudentId(), dto.getParentId());
        Payment payment = new Payment();

        // Fetch parent and student
        Parent payingParent = parentRepository.findById(dto.getParentId())
                .orElseThrow(() -> {
                    log.error("Parent with ID {} not found", dto.getParentId());
                    return new ParentNotFoundException();
                });

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> {
                    log.error("Student with ID {} not found", dto.getStudentId());
                    return new StudentNotFoundException();
                });

        // Calculate amounts
        BigDecimal originalAmount = dto.getPaymentAmount();
        BigDecimal serviceFee = originalAmount.compareTo(BigDecimal.valueOf(200000)) > 0
                ? originalAmount.multiply(PROCESSING_FEE_DISCOUNTED_RATE)
                : originalAmount.multiply(PROCESSING_FEE_DEFAULT_RATE);
        BigDecimal adjustedAmount = originalAmount.add(serviceFee);

        log.info("Original Amount: {}, Service Fee: {}, Adjusted Amount: {}", originalAmount, serviceFee, adjustedAmount);

        payment.setOriginalAmount(originalAmount);
        payment.setServiceFee(serviceFee);
        payment.setAdjustedAmount(adjustedAmount);

        PaymentType paymentType = student.getPaymentType();

        if (paymentType == PaymentType.UNIQUE) {
            log.info("Processing unique payment for Parent ID: {}", payingParent.getId());
            payment.setPaymentType(PaymentType.UNIQUE);

            // Ensure the parent is associated with the student
            if (student.getParents().stream().noneMatch(parent -> parent.getId().equals(payingParent.getId()))) {
                log.error("Parent ID {} is not associated with Student ID {}", dto.getParentId(), dto.getStudentId());
                throw new InvalidUserIdException("Parent is not associated with the student");
            }

            // Ensure parent has enough balance
            if (payingParent.getBalance().compareTo(adjustedAmount) < 0) {
                log.error("Parent with ID {} does not have sufficient balance", payingParent.getId());
                throw new InsufficientBalanceException("Parent does not have sufficient balance.");
            }

            // Deduct and update
            payingParent.setBalance(payingParent.getBalance().subtract(adjustedAmount));
            student.setBalance(student.getBalance().add(originalAmount));

            parentRepository.save(payingParent);
            studentRepository.save(student);

            log.info("Unique payment processed. Parent new balance: {}", payingParent.getBalance());


        } else if (paymentType == PaymentType.SHARED) {
            log.info("Processing shared payment for Student ID: {}", student.getId());
            payment.setPaymentType(PaymentType.SHARED);

            Set<Parent> studentParents = student.getParents();
            if (studentParents.size() != 2) {
                log.error("Shared payment requires exactly two parents, found: {}", studentParents.size());
                throw new IllegalStateException("Shared payment requires exactly two parents.");
            }

            BigDecimal halfAmount = adjustedAmount.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
            List<Parent> parentsList = new ArrayList<>(studentParents);
            Parent parent1 = parentsList.get(0);
            Parent parent2 = parentsList.get(1);

            if (parent1.getBalance().compareTo(halfAmount) < 0 || parent2.getBalance().compareTo(halfAmount) < 0) {
                log.error("One or both parents have insufficient balance. Required per parent: {}", halfAmount);
                throw new InsufficientBalanceException("Both parents must have at least half the amount.");
            }

            // Deduct from both parents
            parent1.setBalance(parent1.getBalance().subtract(halfAmount));
            parent2.setBalance(parent2.getBalance().subtract(halfAmount));

            parentRepository.save(parent1);
            parentRepository.save(parent2);

            // Credit student with original amount (excluding service fee)
            student.setBalance(student.getBalance().add(originalAmount));
            studentRepository.save(student);

            log.info("Shared payment completed. Parent1 ID: {}, Parent2 ID: {}", parent1.getId(), parent2.getId());

        } else {
            log.error("Invalid payment type encountered: {}", paymentType);
            throw new InvalidPaymentTypeException("Invalid payment Type. It must be either UNIQUE or SHARED.");
        }

        // Finalize and return response
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setParent(payingParent);
        payment.setStudent(student);

        Payment savedPayment = paymentRepository.save(payment);
        PaymentResponse response = modelMapper.map(savedPayment, PaymentResponse.class);

        log.info("Payment processed successfully with ID: {}", savedPayment.getId());
        return new BaseResponse<>(ResponseCodes.SUCCESS, SUCCESS_MESSAGE, response);
    }




}
