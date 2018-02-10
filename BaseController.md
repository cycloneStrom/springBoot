private String getDownLoadFileName(HttpServletRequest request ,String filename) {
        String new_filename = null;
        try {
            new_filename = URLEncoder.encode(filename, "UTF8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String userAgent = request.getHeader("User-Agent");
        // System.out.println(userAgent);
        String rtn = "filename=\"" + new_filename + "\"";
        // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            if (userAgent.indexOf("msie") != -1) {
                rtn = "filename=\"" + new_filename + "\"";
            }
            // Opera浏览器只能采用filename*
            else if (userAgent.indexOf("opera") != -1) {
                rtn = "filename*=UTF-8''" + new_filename;
            }
            // Safari浏览器，只能采用ISO编码的中文输出
            else if (userAgent.indexOf("safari") != -1) {
                try {
                    rtn = "filename=\""
                            + new String(filename.getBytes("UTF-8"),
                            "ISO8859-1") + "\"";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
            else if (userAgent.indexOf("applewebkit") != -1) {
                try {
                    new_filename = MimeUtility
                            .encodeText(filename, "UTF8", "B");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                rtn = "filename=\"" + new_filename + "\"";
            }
            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            else if (userAgent.indexOf("mozilla") != -1) {
                rtn = "filename*=UTF-8''" + new_filename;
            }
        }
        return rtn;
    }