package apijson.demo.server.config;

import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hjt
 * @date 2018/8/30
 * @description
 */
public class ConfigYml {
    public Map read() {
        String fileName = this.getClass().getClassLoader().getResource("application.yml").getPath();//获取文件路径
        File dumpFile=new File(fileName);
        Map father = null;
        try {
            father = Yaml.loadType(dumpFile, HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return father;
    }
}
