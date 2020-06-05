package com.kevinadam.paintcalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_ROOM_REQUEST = 1;
    public static final int EDIT_ROOM_REQUEST = 2;

    ArrayList<Room> mItems  = new ArrayList<>();
    RecyclerView Rooms;
    RecyclerView.LayoutManager mRoomsManager;
    MainAdapter adapter;
    Button btn_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rooms = findViewById(R.id.Rooms);

//        mItems.add(new Room(1,"Kamar", 3.0, 3.0,3.0,1.0));

        loadStorage();

        adapter =  new MainAdapter(mItems);
        Rooms.setHasFixedSize(true);
        mRoomsManager = new LinearLayoutManager(this);
        Rooms.setLayoutManager(mRoomsManager);
        Rooms.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                saveStorage();
            }
        }).attachToRecyclerView(Rooms);

        adapter.setOnItemClickListener(new MainAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Room room) {
                Intent intent = new Intent(MainActivity.this, NewEditRoom.class);
                intent.putExtra(NewEditRoom.EXTRA_ID, room.getId());
                intent.putExtra(NewEditRoom.EXTRA_RNAME, room.getName());
                intent.putExtra(NewEditRoom.EXTRA_LEBAR, room.getWidth().toString());
                intent.putExtra(NewEditRoom.EXTRA_PANJANG, room.getHeight().toString());
                intent.putExtra(NewEditRoom.EXTRA_TINGGI, room.getLength().toString());
                startActivityForResult(intent, EDIT_ROOM_REQUEST);
            }
        });

        btn_new = findViewById(R.id.button_new);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewRoom();
//                mItems.add(new Room("KamarNew", 3, 3,3,5));
//                Rooms.setAdapter(mRoomsAdapter);
            }
        });
    }

    public void openNewRoom() {
        Intent intent = new Intent(this, NewEditRoom.class);
        startActivityForResult(intent, ADD_ROOM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_ROOM_REQUEST && resultCode ==  RESULT_OK){
            String rname = data.getStringExtra(NewEditRoom.EXTRA_RNAME);
            String panjang = data.getStringExtra(NewEditRoom.EXTRA_PANJANG);
            String lebar = data.getStringExtra(NewEditRoom.EXTRA_LEBAR);
            String tinggi = data.getStringExtra(NewEditRoom.EXTRA_TINGGI);

            Double paintNeeds = getPaintNeeds(Double.parseDouble(panjang),  Double.parseDouble(lebar), Double.parseDouble(tinggi));

            mItems.add(new Room(mItems.size()+1,rname, Double.parseDouble(panjang), Double.parseDouble(lebar), Double.parseDouble(tinggi), paintNeeds));
            Toast.makeText(this, "Ruangan berhasil ditambahkan", Toast.LENGTH_SHORT).show();

        }else if(requestCode == EDIT_ROOM_REQUEST && resultCode ==  RESULT_OK){
            int id =  data.getIntExtra(NewEditRoom.EXTRA_ID, -1);

            if (id < 0){
                Toast.makeText(this, "Ruangan gagal diubah", Toast.LENGTH_SHORT).show();
                return;
            }

            String rname = data.getStringExtra(NewEditRoom.EXTRA_RNAME);
            String panjang = data.getStringExtra(NewEditRoom.EXTRA_PANJANG);
            String lebar = data.getStringExtra(NewEditRoom.EXTRA_LEBAR);
            String tinggi = data.getStringExtra(NewEditRoom.EXTRA_TINGGI);

            Double paintNeeds = getPaintNeeds(Double.parseDouble(panjang),  Double.parseDouble(lebar), Double.parseDouble(tinggi));

//            adapter.updateItem(id -1 , new Room(id, rname, Double.parseDouble(panjang), Double.parseDouble(lebar), Double.parseDouble(tinggi), 1.5));
            mItems.set(id -1, new Room(id, rname, Double.parseDouble(panjang), Double.parseDouble(lebar), Double.parseDouble(tinggi), paintNeeds));
            adapter.updatedItem(id-1);

        } else{
            Toast.makeText(this, "Ruangan gagal ditambahkan", Toast.LENGTH_SHORT).show();
        }

        saveStorage();
    }

    private void saveStorage(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mItems);
        editor.putString("room list", json);
        editor.apply();
    }

    private void loadStorage(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString("room list", null);
        Type type = new TypeToken<ArrayList<Room>>(){}.getType();
        mItems = gson.fromJson( json, type);

        if (mItems== null){
            mItems = new ArrayList<>();
        }
    }

    private Double getPaintNeeds(double panjang, double lebar, double tinggi){
        // formula https://www.pratamabangunan.com/id/tips-and-trick/panduan-penghitungan-kebutuhan-cat

        // Hitung luas permukaan bidang dinding atau tembok yang akan dicat.
        // Tinggi x Lebar x Jumlah dinding | contoh Dinding: 4m x 5m x 4 dinding = 80m2

        Integer wall_num = 4;
        Double wall_calc = panjang * lebar * wall_num;

        // Hitung luas bidang permukaan plafon.
        // Panjang x Lebar | contoh Plafon: 3m x 4m = 12m2
        Double plafon_calc = panjang * lebar;

        // jumlahkan total luas bidang tersebut.
        // Total luas dinding + Total luas plafon | contoh 80m2 + 12m2 = 92m2
        Double surface_area = wall_calc + plafon_calc;

        // Hitung luas bidang yang dicat dengan cara kurangi luas pintu dan jendela (jika ada).
        // Total luas dinding & plafon – Luas pintu & jendela | Contoh Luas pintu: 2m x 0.5m = 1m2 -> 92m2 – 1 m2 = 91m2

        // Luas bidang yang akan dicat dibagi dengan daya sebar cat per liter.
        // Satu galon memiliki daya sebar teoritis 12m2 dengan 2 lapis pengecatan,
        // maka jumlah yang dibutuhkan secara teoritis sebagai berikut.
        // (Total luas bidang : Daya sebar teoritis) x Jumlah lapisan pengecatan | contoh (91m2 : 12) x 2 = 15,2 liter
        Integer scattering = 12;
        Integer layers = 2;
        Double total = (surface_area / scattering) * layers;

        return total;
    }

}
