package net.haichat.factory.utils;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * Gson 对 DBFlow 数据库的 Bean 过滤字段
 */
public class DBFlowExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        // 被跳过字段
        // 只要是属于 DBFlow 数据库的 Bean 就被跳过 不解析
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        // 被跳过的 Class
        return false;
    }
}
