package br.edu.ufersa.cc.sd.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Constants {

    public static final String DEFAULT_HOST = getDefaultHost();
    public static final Integer LOCALIZATION_PORT = 8484;

    private static final String getDefaultHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

}
