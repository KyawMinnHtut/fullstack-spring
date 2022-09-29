package com.jdc.leaves.model.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jdc.leaves.model.dto.input.ClassForm;
import com.jdc.leaves.model.dto.input.LeaveForm;
import com.jdc.leaves.model.dto.output.LeaveListVO;
import com.jdc.leaves.model.dto.output.LeaveSummaryVO;
import com.jdc.leaves.model.dto.output.StudentListVO;

@Service
public class LeaveService {

	private NamedParameterJdbcTemplate template;
	private SimpleJdbcInsert insert;
	
	private static final String LEAVE_COUNT_SQL = """
			select count(leave_date) from leaves_day 
			where leave_date = :target and leaves_classes_id = :classId
			""";
	private static final String SELECT_PROJECTION = """
			select l.apply_date applyDate, l.classes_id classId, l.student_id studentId, 
			a.name student, s.phone studentPhone, c.teacher_id teacherId, 
			at.name teacher, l.start_date startDate, l.days, l.reason 
			from leaves l join student s on l.student_id = s.id 
			join account a on l.student_id = a.id 
			join classes c on l.classes_id = c.id 
			left join account at on c.teacher_id = at.id
			""";

	@Autowired
	private ClassService classService;

	public LeaveService(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
		insert = new SimpleJdbcInsert(dataSource);
		
		insert.setTableName("leaves");
	}

	public List<LeaveListVO> search(Optional<Integer> classId, Optional<Integer> studentId, Optional<String> from,
			Optional<String> to) {
		
		var sb = new StringBuffer(SELECT_PROJECTION);
		sb.append(" where 1 = 1");
		var params = new HashMap<String, Object>();
		
		sb.append(classId.filter(Objects::nonNull).map(a -> {
			params.put("class_id", a);
			return " and l.classes_id = :class_id";
		}).orElse(""));
		
		sb.append(studentId.filter(Objects::nonNull).map(a -> {
			params.put("student_id", a);
			return " and l.student_id = :student_id";
		}).orElse(""));

		sb.append(from.filter(StringUtils::hasText).map(a -> {
			params.put("from", Date.valueOf(a));
			return " and l.start_date >= :from";
		}).orElse(""));

		sb.append(to.filter(StringUtils::hasText).map(a -> {
			params.put("to", Date.valueOf(a));
			return " and l.start_date <= :to";
		}).orElse(""));

		sb.append(" ").append(" order by l.apply_date");
		return template.query(sb.toString(), params, new BeanPropertyRowMapper<>(LeaveListVO.class));
}

	@Transactional
	public void save(LeaveForm form) {
			insert.execute(Map.of(
				"apply_date", form.getApplyDate(),
				"classes_id", form.getClassId(),
				"student_id", form.getStudent(),
				"start_date", Date.valueOf(form.getStartDate()),
				"days", form.getDays(),
				"reason", form.getReason()
		));
	}

	public List<LeaveSummaryVO> searchSummary(Optional<LocalDate> target) {

		// Find Classes
		var classes = classService.search(Optional.ofNullable(null), Optional.ofNullable(null),
				Optional.ofNullable(null));
		
		var result = classes.stream().map(LeaveSummaryVO::new).toList();
		
		for(var vo : result) {
			vo.setLeaves(findLeavesForClass(vo.getClassId(), target.orElse(LocalDate.now())));
		}

		return result;
	}

	private long findLeavesForClass(int classId, LocalDate date) {
		return template.queryForObject(LEAVE_COUNT_SQL, 
				Map.of("classId", classId, "target", Date.valueOf(date)), Long.class);
	}

}