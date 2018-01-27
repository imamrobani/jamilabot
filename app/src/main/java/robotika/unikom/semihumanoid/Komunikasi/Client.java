package robotika.unikom.semihumanoid.Komunikasi;

import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by Dena Meidina on 02/01/2017.
 */

public class Client{

    //=======================================Variable===============================================
    //Socket
    private Socket socket;
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public Client(){
        //do nothing
    }
    //==================================End_Constructor=============================================

    public synchronized void setData(String... s){
        try {
            if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                new SendData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
            } else {
                new SendData().execute(s);
            }
        }catch (RejectedExecutionException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    public class SendData extends AsyncTask<String, Void, String>{
        //================================Implement_AsyncTask===========================================
        @Override
        protected String doInBackground(String... params) {
            if(params[0].equals("client")) {
                try {
                    socket = new Socket(Komunikasi.ipServer, Komunikasi.portForStatus);

                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(params[1]);
                    out.flush();
                    out.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }
        //================================Implement_AsyncTask===========================================
    }
}
