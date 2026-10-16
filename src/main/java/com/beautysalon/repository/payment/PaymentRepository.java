package com.beautysalon.repository.payment;

import com.beautysalon.model.payment.*;
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

@Repository
public class PaymentRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 13;

    @Autowired
    public PaymentRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("payments.txt"); }

    private Payment fromParts(String[] p) {
        Payment pay = new Payment();
        pay.setId(p[0]);
        pay.setAppointmentId(p[1]);
        pay.setCustomerId(p[2]);
        pay.setAmount(new BigDecimal(p[3]));
        pay.setDiscount(new BigDecimal(p[4]));
        pay.setTax(new BigDecimal(p[5]));
        pay.setTotal(new BigDecimal(p[6]));
        pay.setPaymentMethod(PaymentMethod.fromString(p[7]));
        pay.setPaymentStatus(PaymentStatus.fromString(p[8]));
        pay.setTransactionRef(p[9]);
        pay.setNotes(p[10]);
        pay.setCreatedAt(DateTimeUtil.parseDateTime(p[11]));
        pay.setUpdatedAt(DateTimeUtil.parseDateTime(p[12]));
        return pay;
    }

    private String toLine(Payment p) {
        return FileStorageUtil.joinLine(
            p.getId(), p.getAppointmentId(), p.getCustomerId(),
            p.getAmount().toPlainString(), p.getDiscount().toPlainString(),
            p.getTax().toPlainString(), p.getTotal().toPlainString(),
            p.getPaymentMethod().name(), p.getPaymentStatus().name(),
            p.getTransactionRef() != null ? p.getTransactionRef() : "",
            p.getNotes() != null ? p.getNotes() : "",
            DateTimeUtil.formatDateTime(p.getCreatedAt()),
            DateTimeUtil.formatDateTime(p.getUpdatedAt())
        );
    }

    public List<Payment> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public Optional<Payment> findById(String id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Payment> findByAppointmentId(String appointmentId) {
        return findAll().stream().filter(p -> appointmentId.equals(p.getAppointmentId())).findFirst();
    }

    public List<Payment> findByCustomerId(String customerId) {
        return findAll().stream().filter(p -> customerId.equals(p.getCustomerId())).toList();
    }

    public Payment save(Payment p) throws IOException {
        if (p.getId() == null || p.getId().isBlank()) p.setId(idGenerator.generate());
        if (p.getCreatedAt() == null) p.setCreatedAt(DateTimeUtil.now());
        p.setUpdatedAt(DateTimeUtil.now());
        FileStorageUtil.appendLine(file(), toLine(p));
        return p;
    }

    public Payment update(Payment p) throws IOException {
        p.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(p);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && p.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return p;
    }
}
