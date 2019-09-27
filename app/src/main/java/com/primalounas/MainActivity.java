package com.primalounas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.primalounas.Networking.GetFoodMenuService;
import com.primalounas.Objects.MenuItem;

import org.apache.poi.hwpf.HWPFDocument;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.layout_loading) LinearLayout layoutLoading;
    @BindView(R.id.layout_loaded) ScrollView layoutLoaded;
    @BindView(R.id.layout_error) LinearLayout layoutError;

    @BindView(R.id.textview_loadingtext) TextView textLoading;
    @BindView(R.id.textview_errortext) TextView textError;

    @BindView(R.id.textview_saladprice) TextView textSaladPrice;
    @BindView(R.id.textview_foodprice) TextView textFoodPrice;
    @BindView(R.id.textview_soupprice) TextView textSoupPrice;

    @BindView(R.id.recyclerview_foodcourses) RecyclerView recyclerFoodCourses;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerFoodCourses.setLayoutManager(layoutManager);

        itemAdapter = new ItemAdapter<MenuItem>();
        FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        recyclerFoodCourses.setAdapter(fastAdapter);

        fetchMenu();
    }

    private void fetchMenu(){
        showLoading();

        textLoading.setText("Hold on I'm fetching the menu!");

        new CountDownTimer(5000, 5000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                textLoading.setText("This is taking longer than expected... I'm gonna blame yamflex for this one.");
            }
        }.start();

        new CountDownTimer(15000, 15000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                textLoading.setText("Still trying to load... The problem is probably either your internet connection or google's servers... \n\nOf course it's google's fault!");
            }
        }.start();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://drive.google.com/").build();
        GetFoodMenuService service = retrofit.create(GetFoodMenuService.class);
        service.getFoodMenu("https://drive.google.com/uc?id=0B8nQh-fa3RbLMFN0X1QxaDFhYzQ&export=download").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    BufferedInputStream in = new BufferedInputStream(response.body().byteStream());
                    HWPFDocument doc = new HWPFDocument(in);
                    fetchedMenuSuccess(doc);
                }
                catch (Exception ex){
                    fetchedMenuFailure(ex.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                fetchedMenuFailure(t.getMessage());
            }
        });
    }

    private void fetchedMenuSuccess(HWPFDocument document){
        processDocumentText(document.getDocumentText());
        showResults();
    }

    private void fetchedMenuFailure(String exception){
        textError.setText(exception);
        showError();
    }

    private void showLoading(){
        layoutLoading.setVisibility(View.VISIBLE);
        layoutLoaded.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
    }

    private void showError(){
        layoutLoading.setVisibility(View.GONE);
        layoutLoaded.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
    }

    private void showResults(){
        layoutLoading.setVisibility(View.GONE);
        layoutLoaded.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void processDocumentText(String text){
        text = text.trim().replaceAll("\r+", "\t").replaceAll(" +", " ").replaceAll("\t{2,}|\t+ +\t+", "\t");
        String[] splits = text.split("\t");

        String companyName = splits[0];
        String saladPrice = splits[1];
        String restaurantName = splits[2];
        String foodPrice = splits[3];
        String phoneNumber = splits[4];
        String soupPrice = splits[5];
        String title = splits[6];

        String monday = splits[7];
        String mondaySalad = splits[8];
        String mondayFood = splits[9];
        String mondaySoup = splits[10];

        String tuesday = splits[11];
        String tuesdaySalad = splits[12];
        String tuesdayFood = splits[13];
        String tuesdaySoup = splits[14];

        String wednesday = splits[15];
        String wednesdaySalad = splits[16];
        String wednesdayFood = splits[17];
        String wednesdaySoup = splits[18];

        String thursday = splits[19];
        String thursdaySalad = splits[20];
        String thursdayFood = splits[21];
        String thursdaySoup = splits[22];

        String friday = splits[23];
        String fridaySalad = splits[24];
        String fridayFood = splits[25];
        String fridaySoup = splits[26];

        textSaladPrice.setText(saladPrice);
        textFoodPrice.setText(foodPrice);
        textSoupPrice.setText(soupPrice);

        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem(monday, mondaySalad, mondayFood, mondaySoup));
        items.add(new MenuItem(tuesday, tuesdaySalad, tuesdayFood, tuesdaySoup));
        items.add(new MenuItem(wednesday, wednesdaySalad, wednesdayFood, wednesdaySoup));
        items.add(new MenuItem(thursday, thursdaySalad, thursdayFood, thursdaySoup));
        items.add(new MenuItem(friday, fridaySalad, fridayFood, fridaySoup));

        itemAdapter.add(items);

        this.setTitle("Prima lounas, " + title);
    }
}