#!/bin/bash

# validate-jdbc.sh — CLI test harness for JDBC interactivity via JShell

# Ensure JDBC connector is present
JDBC_JAR="mysql-connector-java-8.x.x.jar"
if [ ! -f "$JDBC_JAR" ]; then
  echo "JDBC connector not found: $JDBC_JAR"
  echo "Place the JAR in the current directory or update the script path."
  exit 1
fi

# Launch JShell with JDBC and compiled classes
echo "Launching JShell with JDBC and compiled classes..."
jshell --class-path "target/classes:$JDBC_JAR" <<EOF
import java.sql.DriverManager;
var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gcash_db", "your_user", "your_pass");
var tx = new Transactions(conn);
tx.viewUserAll(1);
EOF

echo "JDBC interactivity test completed."
