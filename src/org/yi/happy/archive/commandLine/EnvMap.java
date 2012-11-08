package org.yi.happy.archive.commandLine;
import java.util.Map;
import java.util.Properties;

public class EnvMap {
    public static void main (String[] args) {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }

        Properties prop = System.getProperties();
        for (Map.Entry<Object, Object> e : prop.entrySet()) {
            System.out.format("%s=%s%n", e.getKey(), e.getValue());
        }
    }
}