package com.suryani.manage.system.service;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.suryani.manage.db.NativeDao;
import com.suryani.manage.db.RsExtractor4MapList;

@Service
public class MenuGetService {
    @Inject
    private NativeDao nativeDao;

    public List<Map<String, Object>> getMenusByRoleId(String roleId) {
        return this.nativeDao.getJdbcTemplate().query("select t1.* from system_role_menu t join system_menu t1 on t1.id=t.menu_id where t.role_id=? order by t1.level,t1.sort desc",
                new Object[] { roleId }, new int[] { Types.VARCHAR }, new RsExtractor4MapList());
    }
}
