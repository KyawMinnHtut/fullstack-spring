package com.jdc.leaves.model.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private SimpleJdbcInsert leavesInsert;
	private SimpleJdbcInsert leavesDaysInsert;
	
	private static final String LEAVE_COUNT_SQL = """
			select count(leave_date) from leaves_day 
			where leave_date = :target and leaves_classes_id = :classId
			""";
	private static final String SELECT_PROJECTION = """
			select distinct l.apply_date applyDate, l.classes_id classId, l.student_id studentId, l.start_date startDate, 
			l.days, l.reason, sa.name student, s.phone studentPhone, c.teacher_id teacherId, ta.name teacher 
			from leaves l join classes c on l.classes_id = c.id 
			join teacher t on c.teacher_id = t.id 
			join account ta on t.id = ta.id 
			join student s on l.student_id = s.id 
			join account sa on s.id = sa.id 
			join leaves_day ld on l.apply_date = ld.leaves_apply_date and l.classes_id = ld.leaves_classes_id and l.student_id = ld.leaves_student_id
			""";

	@Autowired
	private ClassService classService;

	public LeaveService(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
		
		leavesInsert = new SimpleJdbcInsert(dataSource);		
		leavesInsert.setTableName("leaves");
		
		leavesDaysInsert = new SimpleJdbcInsert(dataSource);
		leavesDaysInsert.setTableName("leaves_day");
	}

	public List<LeaveListVO> search(Optional<Integer> classId, Optional<String> from,
			Optional<String> to) {
		
		var sb = new StringBuffer(SELECT_PROJECTION);
		sb.append(" where 1 = 1");
		var params = new HashMap<String, Object>();
		
//		if student, should reference his own data
		var token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		if (token.getAuthorities().contains(AuthorityUtils.commaSeparatedStringToAuthorityList("Student").get(0))) {
			params.put("login", token.getName());			
			sb.append(" and sa.email = :login");
		}
		
		sb.append(classId.filter(Objects::nonNull).map(a -> {
			params.put("classId", a);
			return " and l.classes_id = :classId";
		}).orElse(""));


		sb.append(from.filter(StringUtils::hasText).map(a -> {
			params.put("from", Date.valueOf(a));
			return " and ld.leaveDate >= :from";
		}).orElse(""));

		sb.append(to.filter(StringUtils::hasText).map(a -> {
			params.put("to", Date.valueOf(a));
			return " and ld.leaveDate <= :to";
		}).orElse(""));

		sb.append(" ").append(" order by l.start_date, l.apply_date, sa.name");
		return template.query(sb.toString(), params, new BeanPropertyRowMapper<>(LeaveListVO.class));
}

	@Transactional
	public void save(LeaveForm form) {
//		Insert into leaves table  
			leavesInsert.execute(Map.of(
				"apply_date", Date.valueOf(form.getApplyDate()),
				"classes_id", form.getClassId(),
				"student_id", form.getStudent(),
				"start_date", Date.valueOf(form.getStartDate()),
				"days", form.getDays(),
				"reason", form.getReason()
					));
//			Insert into leaves_days table
			var leaveDates = IntStream.iterate(0, a-> a+1).limit(form.getDays()).mapToObj(a -> form.getStartDate().plusDays(a)).toList();
			for (var leaveDate : leaveDates) {
				leavesDaysInsert.execute(Map.of(
						"leave_date", Date.valueOf(leaveDate),
						"leaves_apply_date", Date.valueOf(form.getApplyDate()),
						"leaves_classes_id", form.getClassId(),
						"leaves_student_id", form.getStudent()
						));
			}
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