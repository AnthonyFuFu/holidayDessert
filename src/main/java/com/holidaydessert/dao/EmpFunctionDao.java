package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.EmpFunction;

@Repository
public class EmpFunctionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// =============================================
	// back
	// =============================================
	public void add(EmpFunction empFunction) {

		String sql = " INSERT INTO holiday_dessert.emp_function "
				   + " (FUNC_NAME, FUNC_LAYER, FUNC_PARENT_ID, FUNC_LINK, FUNC_STATUS, FUNC_ICON) "
				   + " VALUES(?, ?, ?, ?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] {empFunction.getFuncName(), empFunction.getFuncLayer(), empFunction.getFuncParentId(), empFunction.getFuncLink(), empFunction.getFuncStatus(), empFunction.getFuncIcon() });
		
	}

	public void update(EmpFunction empFunction) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.emp_function "
				   + " SET FUNC_NAME = ?, FUNC_LAYER = ?, FUNC_PARENT_ID = ?, FUNC_LINK = ?, FUNC_STATUS = ?, FUNC_ICON = ? "
				   + " WHERE FUNC_ID = ? ";
		
		args.add(empFunction.getFuncName());
		args.add(empFunction.getFuncLayer());
		args.add(empFunction.getFuncParentId());
		args.add(empFunction.getFuncLink());
		args.add(empFunction.getFuncStatus());
		args.add(empFunction.getFuncIcon());
		args.add(empFunction.getFuncId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	public EmpFunction getData(EmpFunction empFunction) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.emp_function "
				   + " WHERE FUNC_ID = ? ";
		
		args.add(empFunction.getFuncId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		EmpFunction function = new EmpFunction();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        String funcId = String.valueOf(resultMap.get("FUNC_ID"));
	        String funcName = String.valueOf(resultMap.get("FUNC_NAME"));
	        String funcLayer = String.valueOf(resultMap.get("FUNC_LAYER"));
	        String funcParentId = String.valueOf(resultMap.get("FUNC_PARENT_ID"));
	        String funcLink = String.valueOf(resultMap.get("FUNC_LINK"));
	        String funcStatus = String.valueOf(resultMap.get("FUNC_STATUS"));
	        String funcIcon = String.valueOf(resultMap.get("FUNC_ICON"));
	        
	        function.setFuncId(funcId);
	        function.setFuncName(funcName);
	        function.setFuncLayer(funcLayer);
	        function.setFuncParentId(funcParentId);
	        function.setFuncLink(funcLink);
	        function.setFuncStatus(funcStatus);
	        function.setFuncIcon(funcIcon);
	    }
		return function == null ? null : function;
	}

	public List<Map<String, Object>> getAdminListToAuth() {

		String sql = " SELECT FUNC_ID FROM holiday_dessert.emp_function "
				   + " WHERE FUNC_STATUS = 1 ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}
