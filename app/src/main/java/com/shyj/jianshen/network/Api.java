package com.shyj.jianshen.network;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    /**
     * 课程推荐页的不同类型的推荐
     * type	字符串	是	1 Abs Workout，2 Fat Burning，3 Stretch，4 Chest Workout，5 Warm Up
     */
    @FormUrlEncoded
    @POST("/v1/course/recommendCourseList")
    Observable<String> getRecommendCourseList(@FieldMap Map<String, Object> params);

    /**
     * 课程搜索接口
     * keyWord	字符串	是	搜索关键词
     * pageNum	整型	是	页码
     */
    @FormUrlEncoded
    @POST("/v1/course/searchCourseList")
    Observable<String> getSearchCourseList(@FieldMap Map<String, Object> params);

    /**
     * 按分类查询课程列表
     * grade	字符串	是	课程等级，分为1,2,3,4等级
     * pageNum	整型	是	页码
     */
    @FormUrlEncoded
    @POST("/v1/course/selectCourseList")
    Observable<String> getSelectCourseList(@FieldMap Map<String, Object> params);

    /**
     * 固定推荐的计划
     * pageNum	整型	是	页码
     */
    @FormUrlEncoded
    @POST("/v1/plan/selectPlanRecommend")
    Observable<String> getSelectPlanRecommend(@FieldMap Map<String, Object> params);

    /**
     * grade	整型	是	1初学者 2中级 3进阶
     * pageNum	整型	是	页面，从1开始
     * */
    @FormUrlEncoded
    @POST("/v1/plan/selectPlansByGrade")
    Observable<String> getSelectPlanGrade(@FieldMap Map<String, Object> params);

    /**
     * 智能推荐的计划
     * sex	整型	是	性别，1男 2女
     * grade	整型	是	课程等级，分为1,2,3等级
     * purpose	整型	是	1塑性 2增肌 3减肥
     * bodyParts	整型	是	1全身 2腹部 3腿 4臀 5手臂
     */
    @FormUrlEncoded
    @POST("/v1/plan/selectPlanSmartRecommend")
    Observable<String> getSelectPlanSmartRecommend(@FieldMap Map<String, Object> params);

    /**
     * 查询背景音乐列表
     * */
    @FormUrlEncoded
    @POST("/v1/music/selectMusicList")
    Observable<String> getSelectMusicList(@FieldMap Map<String, Object> params);
}
