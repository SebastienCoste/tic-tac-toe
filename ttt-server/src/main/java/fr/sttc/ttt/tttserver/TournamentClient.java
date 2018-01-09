package fr.sttc.ttt.tttserver;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TournamentClient {

    private OkHttpClient client;
    private String url;
    private Boolean active;
    private String gameId;

    public TournamentClient(String url, String gameId){
        this.client = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .writeTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .build();
        this.url = url;
        this.active = true;
        this.gameId = gameId;
    }


    public Integer askForMove(){

        if(!active){
            return null;
        }


        Request request = new Request.Builder()
                .url(url+"/ask/" + gameId)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return Integer.valueOf(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            active = false;
        }

        return null;
    }

    public void tellMove(Integer move){

        if(!active){
            return;
        }

        Request request = new Request.Builder()
                .url(url+"/move/" + gameId + "/" + move)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            active = false;
        }
    }

    public void tellResult(ResultTournament result){

        if(!active){
            return;
        }

        Request request = new Request.Builder()
                .url(url+"/result/" + gameId + "/" + result.toString().toLowerCase())
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            active = false;
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TournamentClient that = (TournamentClient) o;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
