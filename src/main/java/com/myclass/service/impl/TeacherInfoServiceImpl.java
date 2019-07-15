package com.myclass.service.impl;

import com.myclass.entity.TeacherInfo;
import com.young.encrypt.EncryptTool;
import com.myclass.dao.TeacherInfoMapper;
import com.myclass.service.TeacherInfoService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 教师表(TeacherInfo)(TeacherInfo)表服务实现类
 *
 * @author 蜀山剑仙
 * @since 2019-07-11 16:59:33
 */
@Service("teacherinfoService")
public class TeacherInfoServiceImpl implements TeacherInfoService {

    @Resource
    private TeacherInfoMapper teacherInfoMapper;


    /**
     * 通过md5加密登录
     *
     * @param loginName
     * @param pwd
     * @return com.com.myclass.entity.TeacherInfo
     * @author Joe
     * @date 2019/7/11 17:10
     */
    @Override
    public TeacherInfo login(String loginName, String pwd) throws Exception {
        pwd = EncryptTool.md5(pwd);
        TeacherInfo teacherInfo = teacherInfoMapper.getTeacherInfoByLoginNameAndPwd(loginName, pwd);
        if (teacherInfo != null) {
            teacherInfoMapper.updateLastLoginTimeById(teacherInfo.getId());
        }
        return teacherInfo;
    }

    /**
     * 新增一名教师数据
     *
     * @param teacherInfo
     * @return
     */
    @Override
    public boolean insertTeacherInfo(TeacherInfo teacherInfo) throws Exception {
        try {
            teacherInfo.setPwd(EncryptTool.md5("123456"));
            return teacherInfoMapper.insertTeacherInfo(teacherInfo) > 0;
        } catch (DuplicateKeyException e) {
            throw new Exception("添加教师时出错:该登录名已经存在");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}