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
        // ���û��UA����Ĭ��ʹ��IE�ķ�ʽ���б��룬��Ϊ�Ͼ�IE����ռ������
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE�������ֻ�ܲ���URLEncoder����
            if (userAgent.indexOf("msie") != -1) {
                rtn = "filename=\"" + new_filename + "\"";
            }
            // Opera�����ֻ�ܲ���filename*
            else if (userAgent.indexOf("opera") != -1) {
                rtn = "filename*=UTF-8''" + new_filename;
            }
            // Safari�������ֻ�ܲ���ISO������������
            else if (userAgent.indexOf("safari") != -1) {
                try {
                    rtn = "filename=\""
                            + new String(filename.getBytes("UTF-8"),
                            "ISO8859-1") + "\"";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            // Chrome�������ֻ�ܲ���MimeUtility�����ISO������������
            else if (userAgent.indexOf("applewebkit") != -1) {
                try {
                    new_filename = MimeUtility
                            .encodeText(filename, "UTF8", "B");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                rtn = "filename=\"" + new_filename + "\"";
            }
            // FireFox�����������ʹ��MimeUtility��filename*��ISO������������
            else if (userAgent.indexOf("mozilla") != -1) {
                rtn = "filename*=UTF-8''" + new_filename;
            }
        }
        return rtn;
    }