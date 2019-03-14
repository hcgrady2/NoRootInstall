package com.example.study.norootinstalldemo.shellService;

/**
 * Created by WenTong on 2019/3/13.
 */


public class ServiceThread extends Thread {
    private static int ShellPORT = 4521;

    @Override
    public void run() {
        System.out.println(">>>>>>Shell Server is start ...<<<<<<");
        new Service(new Service.ServiceGetText() {
            @Override
            public String getText(String text) {
                if (text.startsWith("###AreYouOK")){
                    return "###IamOK#";
                }
                try{
                    ServiceShellUtils.ServiceShellCommandResult sr =  ServiceShellUtils.execCommand(text, false);
                    if (sr.result == 0){
                        return "###ShellOK#" + sr.successMsg;
                    } else {
                        return "###ShellError#" + sr.errorMsg;
                    }
                }catch (Exception e){
                    return "###CodeError#" + e.toString();
                }
            }
        }, ShellPORT);
    }
}