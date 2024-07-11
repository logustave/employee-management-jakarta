package com.employee.management;

import com.employee.management.model.Employee;
import com.employee.management.model.Manager;
import com.employee.management.model.Message;
import com.employee.management.model.Vacation;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.util.EnumSet;

public class GenerateSchema {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // Load configuration from hibernate.cfg.xml
                .build();

        try {
            MetadataSources metadata = new MetadataSources(registry);

            metadata.addAnnotatedClass(Manager.class);
            metadata.addAnnotatedClass(Employee.class);
            metadata.addAnnotatedClass(Message.class);
            metadata.addAnnotatedClass(Vacation.class);

            SchemaExport schemaExport = new SchemaExport();
            schemaExport.setOutputFile("schema.sql");
            schemaExport.setFormat(true);
            schemaExport.execute(EnumSet.of(TargetType.DATABASE), SchemaExport.Action.CREATE, metadata.buildMetadata());
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
