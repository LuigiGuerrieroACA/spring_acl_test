package db.flyway;

import com.example.spring_acl_test.acl.Resource;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class V4__insert_base_resource_type_acl_resource_class extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
//        try (PreparedStatement insert = context.getConnection().prepareStatement(
//                "INSERT INTO acl_class (class, class_id_type) VALUES (?, ?)")) {
//
//            insert.setString(1, Resource.class.getName());
//            insert.setString(2, String.class.getName());
//            insert.executeUpdate();
//        }
    }
}
