package com.natareno.webserviceexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edtCodigo,edtProducto, edtPrecio,edtFabricante;
    Button btnAgregar,btnBuscar, btnEditar,btnEliminar;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCodigo= findViewById(R.id.edtCodigo);
        edtProducto=findViewById(R.id.edtProducto);
        edtPrecio=findViewById(R.id.edtPrecio);
        edtFabricante=findViewById(R.id.edtFabricante);

        btnAgregar=findViewById(R.id.btnAgregar);
        btnBuscar=findViewById(R.id.btnBuscar);
        btnEditar=findViewById(R.id.btnEditar);
        btnEliminar=findViewById(R.id.btnEliminar);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("http://192.168.0.11:80/example/insertar_producto.php");



            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto("http://192.168.0.11:80/example/buscar_producto.php?codigo="+edtCodigo.getText()+"");
            }
        });


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ejecutarServicio("http://192.168.0.11:80/example/editar_producto.php");

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto("http://192.168.0.11:80/example/eliminar_producto.php");

            }
        });

    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                parametros.put("codigo", edtCodigo.getText().toString());
                parametros.put("producto",edtProducto.getText().toString());
                parametros.put("precio",edtPrecio.getText().toString());
                parametros.put("fabricante",edtFabricante.getText().toString());


                return parametros;
            }
        };


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void limpiarCampos(){
        edtCodigo.setText("");
        edtFabricante.setText((""));
        edtPrecio.setText((""));
        edtProducto.setText((""));

    }

    private void buscarProducto(String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        edtProducto.setText(jsonObject.getString("producto"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();

            }
        });

        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);




    }

    private void eliminarProducto(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                parametros.put("codigo", edtCodigo.getText().toString());



                return parametros;
            }
        };


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}
