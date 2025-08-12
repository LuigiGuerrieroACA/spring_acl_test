package db.flyway;

import com.example.spring_acl_test.acl.Resource;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class V5__insert_resource_data extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (PreparedStatement insert = context.getConnection().prepareStatement(
                "INSERT INTO resource (id, reference_class_name, class_id_type) VALUES (?, ?, ?)")) {

            for (Resource resource : Resource.values()) {
                insert.setString(1, resource.id());
                insert.setString(2, resource.className());
                insert.setString(3, resource.idType());
                insert.executeUpdate();
            }
        }
    }
}
