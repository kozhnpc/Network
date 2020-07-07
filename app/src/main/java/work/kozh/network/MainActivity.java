package work.kozh.network;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import work.kozh.request.retrofit.utils.NetLogUtils;
import work.kozh.request.retrofit.Network;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //测试新的网络请求
        test();


    }

    private void test() {

        Network.get("http://103.71.238.176/app/cms/public/?service=App.Mov.SearchVod&key=鬼吹灯", Video.class,
                new Network.NetResult<Video>() {
                    @Override
                    public void onSuccess(Video video) {
                        NetLogUtils.i("网络结果：" + video.getData().size());
                    }

                    @Override
                    public void onError(String msg) {
                        NetLogUtils.i(msg);
                    }
                },
                this);


    }
}
