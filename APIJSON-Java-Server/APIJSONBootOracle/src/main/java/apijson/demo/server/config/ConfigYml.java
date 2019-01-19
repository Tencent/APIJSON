package apijson.demo.server.config;

import org.ho.yaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hjt
 * @date 2018/8/30
 * @description
 */
public class ConfigYml {
    public Map read() {        
        InputStream dumpFile = this.getClass().getClassLoader().getResourceAsStream("application.yml");
        Map father = null;
        try {
            father = Yaml.loadType(dumpFile, HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return father;
    }
}
