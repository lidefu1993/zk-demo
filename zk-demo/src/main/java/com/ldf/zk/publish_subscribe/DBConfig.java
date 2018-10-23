package com.ldf.zk.publish_subscribe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ldf on 2018/10/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBConfig {
    private String url;
    private String driver;
    private String username;
    private String password;
}
