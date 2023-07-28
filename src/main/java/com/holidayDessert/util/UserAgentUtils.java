package com.holidayDessert.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class UserAgentUtils {
	private static final String _I_PHONE_IOS_12 = "iPhone OS 12_";
    private static final String _I_PAD_IOS_12 = "iPad; CPU OS 12_";
    private static final String _MAC_OS_10_14 = " OS X 10_14_";
    private static final String _VERSION = "Version/";
    private static final String _SAFARI = "Safari";
    private static final String _EMBED_SAFARI = "(KHTML, like Gecko)";
    private static final String _CHROME = "Chrome/";
    private static final String _CHROMIUM = "Chromium/";
    private static final String _UC_BROWSER = "UCBrowser/";
    private static final String _ANDROID = "Android";
	/*
     * checks SameSite=None;Secure incompatible Browsers
     * https://www.chromium.org/updates/same-site/incompatible-clients
     */
    public static boolean isSameSiteInCompatibleClient(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(userAgent)) {
            boolean isIos12 = isIos12(userAgent), isMacOs1014 = isMacOs1014(userAgent), isChromeChromium51To66 = isChromeChromium51To66(userAgent), isUcBrowser = isUcBrowser(userAgent);
            return isIos12 || isMacOs1014 || isChromeChromium51To66 || isUcBrowser;
        }
        return false;
    }

    private static boolean isIos12(String userAgent) {
        return StringUtils.contains(userAgent, _I_PHONE_IOS_12) || StringUtils.contains(userAgent, _I_PAD_IOS_12);
    }

    private static boolean isMacOs1014(String userAgent) {
        return StringUtils.contains(userAgent, _MAC_OS_10_14)
            && ((StringUtils.contains(userAgent, _VERSION) && StringUtils.contains(userAgent, _SAFARI))  //Safari on MacOS 10.14
            || StringUtils.contains(userAgent, _EMBED_SAFARI)); // Embedded browser on MacOS 10.14
    }

    private static boolean isChromeChromium51To66(String userAgent) {
        boolean isChrome = StringUtils.contains(userAgent, _CHROME), isChromium = StringUtils.contains(userAgent, _CHROMIUM);
        if (isChrome || isChromium) {
            int version = isChrome ? Integer.valueOf(StringUtils.substringAfter(userAgent, _CHROME).substring(0, 2))
                : Integer.valueOf(StringUtils.substringAfter(userAgent, _CHROMIUM).substring(0, 2));
            return ((version >= 51) && (version <= 66));    //Chrome or Chromium V51-66
        }
        return false;
    }

    private static boolean isUcBrowser(String userAgent) {
        if (StringUtils.contains(userAgent, _UC_BROWSER) && StringUtils.contains(userAgent, _ANDROID)) {
            String[] version = StringUtils.splitByWholeSeparator(StringUtils.substringAfter(userAgent, _UC_BROWSER).substring(0, 7), ".");
            int major = Integer.valueOf(version[0]), minor = Integer.valueOf(version[1]), build = Integer.valueOf(version[2]);
            return ((major != 0) && ((major < 12) || (major == 12 && (minor < 13)) || (major == 12 && minor == 13 && (build < 2)))); //UC browser below v12.13.2 in android
        }
        return false;
    }
}
