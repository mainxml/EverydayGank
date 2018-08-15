package xyz.okxy.everydaygank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    // MainActivity根布局(滑动布局)
    private DrawerLayout mDrawerLayout;

    private static final String ANDROID = "http://gank.io/api/data/Android/10/";
    private static final String IOS = "http://gank.io/api/data/iOS/10/";
    private static final String FRONT_END = "http://gank.io/api/data/前端/10/";
    private static final String RECOMMEND = "http://gank.io/api/data/瞎推荐/10/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 设置Toolbar menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // 获取滑动菜单（根布局）布局对象
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // 获取NavigationView对象
        final NavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null); // 传入一个null参数，这样原本的彩色图标就可以显示出来了

        // NavigationView的head单独设置，因为需要对头像图片监听
        View navHeadView = navView.inflateHeaderView(R.layout.nav_header);
        ImageView navHeadImage = navHeadView.findViewById(R.id.icon_image);
        navHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFragment().show(getSupportFragmentManager(), "bigImage");
            }
        });
        Glide.with(this).load(R.drawable.nav_ic_head).into(navHeadImage);

        // NavigationView子菜单选择监听
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_android:
                        updateFragment(ANDROID);
                        break;
                    case R.id.nav_ios:
                        updateFragment(IOS);
                        break;
                    case R.id.nav_front_end:
                        updateFragment(FRONT_END);
                        break;
                    case R.id.nav_recommend:
                        updateFragment(RECOMMEND);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        // 默认选中“今日干货”
        navView.setCheckedItem(R.id.nav_android);
        updateFragment(ANDROID);
    }

    // 更新Fragment的干货板块到容器
    private void updateFragment(String categoryUrl) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, ListFragment.newInstance(categoryUrl))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_me, menu);
        return true;
    }

    // 左上方菜单选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.about_me:
                Intent intent = new Intent(this, AboutMeActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
