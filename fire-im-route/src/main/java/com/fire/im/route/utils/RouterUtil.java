package com.fire.im.route.utils;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.pojo.dto.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 15:52
 */
@Slf4j
public class RouterUtil {

    public interface Consts {

        /**
         * 用户路由信息前缀
         */
        String ROUTE_INFO_PREFIX = "im:user:route:";

        String USER_INFO_PREFIX = "im:user:info:";

        String USER_ID_PARAMETER = "userId";

        String USER_TOKEN_PARAMETER = "token";

    }

    /**
     * parse server str
     * @param server
     * @return
     */
    public static Server parse(String server) {

       String[] arrs = StringUtils.split(server,":");

       if (arrs.length == 0) {
           throw new IMException("server not available");
       }

       return Server.builder().httpPort(Integer.valueOf(arrs[2])).ip(arrs[0]).socketPort(Integer.valueOf(arrs[1])).build();
    }

    /**
     * 检测地址是否可达
     *
     * @param address
     * @param port
     * @param timeout
     * @return
     */
    public static boolean isConnectable(String address, int port, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port), timeout);
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                return Boolean.FALSE;
            }
        }
    }

}
