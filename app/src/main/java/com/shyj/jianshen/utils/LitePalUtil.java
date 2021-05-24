package com.shyj.jianshen.utils;

import com.shyj.jianshen.bean.UsersBean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class LitePalUtil {

    public static UsersBean select(String id){
        UsersBean usersBean = LitePal.find(UsersBean.class,1);
        return usersBean;
    }
    public static void deleteAll(){
        LitePal.deleteAll(UsersBean.class);
    }
}
