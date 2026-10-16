package com.beautysalon.repository.staff;

import com.beautysalon.model.staff.*;
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.util.*;

@Repository
public class ScheduleRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 11;

    @Autowired
    public ScheduleRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("staff_schedules.txt"); }

    private StaffSchedule fromParts(String[] p) {
        StaffSchedule s = new StaffSchedule();
        s.setId(p[0]);
        s.setStaffId(p[1]);
        if (!p[2].isBlank()) s.setDayOfWeek(DayOfWeek.valueOf(p[2]));
        s.setStartTime(DateTimeUtil.parseTime(p[3]));
        s.setEndTime(DateTimeUtil.parseTime(p[4]));
        s.setBreakStart(DateTimeUtil.parseTime(p[5]));
        s.setBreakEnd(DateTimeUtil.parseTime(p[6]));
        s.setLeaveDate(DateTimeUtil.parseDate(p[7]));
        s.setLeaveDateEnd(DateTimeUtil.parseDate(p[8]));
        s.setType(ScheduleType.fromString(p[9]));
        s.setStatus(ScheduleStatus.fromString(p[10]));
        return s;
    }

    private String toLine(StaffSchedule s) {
        return FileStorageUtil.joinLine(
            s.getId(), s.getStaffId(),
            s.getDayOfWeek() != null ? s.getDayOfWeek().name() : "",
            DateTimeUtil.formatTime(s.getStartTime()),
            DateTimeUtil.formatTime(s.getEndTime()),
            DateTimeUtil.formatTime(s.getBreakStart()),
            DateTimeUtil.formatTime(s.getBreakEnd()),
            DateTimeUtil.formatDate(s.getLeaveDate()),
            DateTimeUtil.formatDate(s.getLeaveDateEnd()),
            s.getType().name(),
            s.getStatus().name()
        );
    }

    public List<StaffSchedule> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public List<StaffSchedule> findByStaffId(String staffId) {
        return findAll().stream()
                .filter(s -> staffId.equals(s.getStaffId()) && s.isActive())
                .toList();
    }

    public List<StaffSchedule> findRegularScheduleByStaffId(String staffId) {
        return findByStaffId(staffId).stream()
                .filter(s -> s.getType() == ScheduleType.REGULAR)
                .toList();
    }

    public List<StaffSchedule> findLeaveByStaffId(String staffId) {
        return findByStaffId(staffId).stream()
                .filter(s -> s.getType() == ScheduleType.LEAVE)
                .toList();
    }

    public Optional<StaffSchedule> findById(String id) {
        return findAll().stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public StaffSchedule save(StaffSchedule s) throws IOException {
        if (s.getId() == null || s.getId().isBlank()) s.setId(idGenerator.generate());
        FileStorageUtil.appendLine(file(), toLine(s));
        return s;
    }

    public StaffSchedule update(StaffSchedule s) throws IOException {
        String updatedLine = toLine(s);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && s.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return s;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }

    public void deleteByStaffId(String staffId) throws IOException {
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 1 && staffId.equals(parts[1])) return null;
            return line;
        });
    }
}
