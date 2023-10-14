package com.example.validacion;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.annotations.concurrent.Background;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DetalleFragment extends Fragment {

    private String url;

    List<SlideItem> slideItems = new ArrayList<>();
    List<SlideItem> slideItemsPrueba = new ArrayList<>();
    List<Mecanicos> listaMecanicos = new ArrayList<>();
    List<Refacciones> listaRefacciones = new ArrayList<>();


    List<ActividadadesUnidad> listaActividadesUnidad = new ArrayList<>();


    private File pdfFileImagenes;


    ViewPager2 viewPager2;
    RecyclerView recyclerViewMecanicos, recyclerViewRefacciones, recyclerViewBitacora;

    TextView tvRefacciones, tvBitacora;


    String marca, modelo, fecha, hora, status, motivo, idventa, telefonousuario, emailusuario, nombreusuario;

    String motorI, vinI, placasI, domicilio, gasolina, anioI, kilometraje;

    Context context;

    public DetalleFragment() {
        // Required empty public constructor
    }

    private Handler sliderHandler = new Handler();


    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalle, container, false);


        slideItemsPrueba.clear();
        slideItemsPrueba.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/010517b32ac5b640cd134d6c4503ff27.jpg", "1"));
        slideItemsPrueba.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/010aca7e324f9966ab767c0128d63a96.jpg", "1"));
        slideItemsPrueba.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/bf8a73d0b93408399677aac957b77ada.jpg", "1"));
        slideItemsPrueba.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/6a59df1b0be0b163a706ee0b752cc78d.jpg", "1"));

        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        FloatingActionButton btnImprimirPdf = rootView.findViewById(R.id.btnImprimirPdf);
        TextView textMarca = rootView.findViewById(R.id.tv1);
        TextView textmotivo = rootView.findViewById(R.id.tv3);
        TextView textfecha = rootView.findViewById(R.id.tv4);
        TextView textstatus = rootView.findViewById(R.id.tvstatus);
        tvBitacora = rootView.findViewById(R.id.tvBitacora);
        recyclerViewRefacciones = rootView.findViewById(R.id.recyclerViewRefacciones);
        recyclerViewMecanicos = rootView.findViewById(R.id.recyclerViewMecanicos);
        recyclerViewBitacora = rootView.findViewById(R.id.recyclerViewBitacora);
        viewPager2 = rootView.findViewById(R.id.ViewPager);


        Bundle bundle = getArguments();
        if (bundle != null) {
            marca = bundle.getString("marca", "");
            modelo = bundle.getString("modelo", "");
            fecha = bundle.getString("fecha_ingreso", "");
            hora = bundle.getString("hora_ingreso", "");
            status = bundle.getString("status", "");
            motivo = bundle.getString("motivo", "");
            idventa = bundle.getString("idventa", "");
            motorI = bundle.getString("motorI", "");
            vinI = bundle.getString("vinI", "");
            placasI = bundle.getString("placasI", "");
            gasolina = bundle.getString("gasolina", "");
            anioI = bundle.getString("anioI", "");
            kilometraje = bundle.getString("kilometraje", "");

            nombreusuario = bundle.getString("nombre", "");
            telefonousuario = bundle.getString("telefono", "");
            emailusuario = bundle.getString("email", "");
            domicilio = bundle.getString("domicilio", "");

            CargarRefacciones(idventa);
            CargarMecanicos(idventa);
            CargarImagenes(idventa);
            CargarBitacora(idventa);
            //   CargarChecks(idventa);

            textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            textmotivo.setText(motivo);


            if (fecha.equals("null") || fecha.isEmpty()) {

                textfecha.setText("No hay fecha estimada");
            } else {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = inputFormat.parse(fecha);

                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                    String fecha_formateada = outputFormat.format(date);


                    try {

                        SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                        Date time = inputFormatHora.parse(hora);

                        SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                        String hora_formateada = outputFormatHora.format(time);

                        textfecha.setText("Ingresado: " + "el " + fecha_formateada + " a las " + hora_formateada);

                    } catch (Exception e) {

                        textfecha.setText("Ingresado: " + "el " + fecha_formateada);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
            int colorAmarillo = ContextCompat.getColor(requireContext(), R.color.amarillo);
            int colorVerde = ContextCompat.getColor(requireContext(), R.color.verde);
            int colorRojo = ContextCompat.getColor(requireContext(), R.color.rojo);
            int colorAzulito = ContextCompat.getColor(requireContext(), R.color.azulitoSuave);
            int colorNegro = ContextCompat.getColor(requireContext(), R.color.black);
            int colorGris = ContextCompat.getColor(requireContext(), R.color.gris);

            if (!status.equals("null") || status.isEmpty()) {
                textstatus.setText("Estatus: " + status.toUpperCase());

                if (status.equalsIgnoreCase("pendiente")) {
                    textstatus.setTextColor(colorAmarillo);
                } else if (status.equalsIgnoreCase("Finalizado")) {
                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Servicios programado")) {
                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Cita programada")) {
                    textstatus.setTextColor(colorAzulito);
                } else if (status.equalsIgnoreCase("Diagnostico")) {
                    textstatus.setTextColor(colorRojo);
                } else if (status.equalsIgnoreCase("En espera")) {
                    textstatus.setTextColor(colorGris);
                } else if (status.equalsIgnoreCase("En servicio")) {
                    textstatus.setTextColor(colorAmarillo);
                } else if (status.equalsIgnoreCase("Prueba de ruta")) {
                    textstatus.setTextColor(colorNegro);
                } else if (status.equalsIgnoreCase("Lavado y detallado")) {
                    textstatus.setTextColor(colorAzulito);
                } else if (status.equalsIgnoreCase("Listo para entrega")) {
                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Entregado")) {
                    textstatus.setTextColor(colorVerde);
                } else {
                    textstatus.setTextColor(colorAzulito);
                }
            } else {
                textstatus.setText("Status no disponible");
                textstatus.setTextColor(colorRojo);
            }
        }

        btnImprimirPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ((listaMecanicos.isEmpty() || listaMecanicos.equals("null") || listaMecanicos.equals(null)) && (listaRefacciones.isEmpty() || listaRefacciones.equals("null") || listaRefacciones.equals(null)) && (listaActividadesUnidad.isEmpty() || listaActividadesUnidad.equals("null") || listaActividadesUnidad.equals(null))) {
                    Toast.makeText(context, "No hay suficientes datos para generar un reporte", Toast.LENGTH_SHORT).show();
                } else {
                    //  generarPDFChecks(listaChecks);
                    generarPDF(listaMecanicos, listaRefacciones, listaActividadesUnidad);
                }


            }
        });


        return rootView;
    }


    private void generarPDFChecks(List<Object> listaChekList) {
        Document document = new Document();

        try {
            File pdfFile = new File(requireContext().getExternalFilesDir(null), "mi_archivoCheck.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            float cellPadding = 5f;
            float cellPaddingUser = 2f;

            Drawable drawable = getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            File tempFile = new File(context.getCacheDir(), "temp_image.png");
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image image = Image.getInstance(tempFile.getAbsolutePath());

            image.setAlignment(Element.ALIGN_LEFT);
            image.scaleAbsolute(45, 45);

            Paragraph paragraphNombre = new Paragraph("SERVICIO TECNICO AUTOMOTRIZ GEORGIO S.A DE C.V", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            Paragraph paragraphCalle = new Paragraph("CALLE 17 SUR #704", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            Paragraph paragraphColonia = new Paragraph("COL. LA PURISIMA, CP:75784", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            Paragraph paragraphEstado = new Paragraph("TEHUACAN PUEBLA", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));

            paragraphNombre.setAlignment(Element.ALIGN_CENTER);
            paragraphCalle.setAlignment(Element.ALIGN_CENTER);
            paragraphColonia.setAlignment(Element.ALIGN_CENTER);
            paragraphEstado.setAlignment(Element.ALIGN_CENTER);

            document.add(image);
            document.add(paragraphNombre);
            document.add(paragraphCalle);
            document.add(paragraphColonia);
            document.add(paragraphEstado);
            tempFile.delete();

            //Tabla info de usuario


            document.add(new Paragraph(" "));
            PdfPTable tableInfoUsuario = new PdfPTable(2);
            tableInfoUsuario.setWidthPercentage(100);

            PdfPCell headerCellDatosCliente = new PdfPCell(new Paragraph("DATOS DEL CLIENTE ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE)));
            headerCellDatosCliente.setBackgroundColor(BaseColor.BLACK); // Establecer el color de fondo a negro
            headerCellDatosCliente.setPadding(cellPadding);
            headerCellDatosCliente.setBorderColor(BaseColor.BLACK);
            headerCellDatosCliente.setBorderWidth(0f);
            tableInfoUsuario.addCell(headerCellDatosCliente);

            PdfPCell headerCellOrdenTrabajo = new PdfPCell(new Paragraph("ORDEN DE TRABAJO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE)));
            headerCellOrdenTrabajo.setBackgroundColor(BaseColor.BLACK); // Establecer el color de fondo a negro
            headerCellOrdenTrabajo.setPadding(cellPadding);
            headerCellOrdenTrabajo.setBorderColor(BaseColor.BLACK);
            headerCellOrdenTrabajo.setBorderWidth(0f);
            tableInfoUsuario.addCell(headerCellOrdenTrabajo);


            PdfPCell cellDatosUsu = new PdfPCell(new Paragraph("Nombre: " + nombreusuario + "\nDireccion: " + domicilio + "\nTelefono:" + telefonousuario + "\nCorreo" + emailusuario));
            cellDatosUsu.setBackgroundColor(BaseColor.WHITE);
            cellDatosUsu.setBorderColor(BaseColor.WHITE);
            cellDatosUsu.setBorderWidth(0f);
            cellDatosUsu.setPadding(cellPadding);
            tableInfoUsuario.addCell(cellDatosUsu);

            PdfPCell cellOrden = new PdfPCell(new Paragraph("Orden de trabajo"));
            cellOrden.setBackgroundColor(BaseColor.WHITE);
            cellOrden.setBorderColor(BaseColor.WHITE);
            cellOrden.setBorderWidth(0f);
            cellOrden.setPadding(cellPadding);
            tableInfoUsuario.addCell(cellOrden);


            document.add(tableInfoUsuario);



            /*
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2);
            float[] columnWidths = {2f, 2f};
            table.setWidths(columnWidths);

            PdfPCell headerCell1 = new PdfPCell(new Phrase("Datos de cliente", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Orden de trabajo", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE)));

            headerCell1.setBackgroundColor(BaseColor.BLACK);
            headerCell2.setBackgroundColor(BaseColor.BLACK);
            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell1.setBorder(Rectangle.NO_BORDER);
            headerCell2.setBorder(Rectangle.NO_BORDER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);

            PdfPCell cell1 = new PdfPCell(new Phrase("Nombre del cliente: " + nombreusuario + "\nTelefono: " + telefonousuario + "\nCorreo: " + emailusuario));
            PdfPCell cell2 = new PdfPCell(new Phrase("Orden de trabajo: #12345"));

            cell1.setBackgroundColor(BaseColor.WHITE);
            cell2.setBackgroundColor(BaseColor.WHITE);
            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell1);
            table.addCell(cell2);
            document.add(table);
*/

            //Tabla informacion de la unidad


            document.add(new Paragraph(" "));
            document.add(new Paragraph("DATOS DE LA UNIDAD", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE)));
            PdfPTable tableInfoUnidad = new PdfPTable(8);
            tableInfoUnidad.setWidthPercentage(100);

            PdfPCell headerCellMarca = new PdfPCell(new Paragraph("MARCA", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellMarca.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellMarca.setPadding(cellPadding);
            headerCellMarca.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellMarca.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellMarca);

            PdfPCell headerCellModelo = new PdfPCell(new Paragraph("MODELO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellModelo.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellModelo.setPadding(cellPadding);
            headerCellModelo.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellModelo.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellModelo);

            PdfPCell headerCellAnioUnidad = new PdfPCell(new Paragraph("AÑO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellAnioUnidad.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellAnioUnidad.setPadding(cellPadding);
            headerCellAnioUnidad.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellAnioUnidad.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellAnioUnidad);

            PdfPCell headerCellPlacas = new PdfPCell(new Paragraph("PLACAS", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellPlacas.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellPlacas.setPadding(cellPadding);
            headerCellPlacas.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellPlacas.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellPlacas);


            PdfPCell headerCellVin = new PdfPCell(new Paragraph("VIN", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellVin.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellVin.setPadding(cellPadding);
            headerCellVin.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellVin.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellVin);

            PdfPCell headerCellMotor = new PdfPCell(new Paragraph("MOTOR", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellMotor.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellMotor.setPadding(cellPadding);
            headerCellMotor.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellMotor.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellMotor);


            PdfPCell headerCellKm = new PdfPCell(new Paragraph("KM", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellKm.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellKm.setPadding(cellPadding);
            headerCellKm.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellKm.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellKm);


            PdfPCell headerCellCombustible = new PdfPCell(new Paragraph("COMBUSTIBLE", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellCombustible.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellCombustible.setPadding(cellPadding);
            headerCellCombustible.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellCombustible.setBorderWidth(0f);
            tableInfoUnidad.addCell(headerCellCombustible);


            PdfPCell cellMarca = new PdfPCell(new Paragraph(marca));
            cellMarca.setBackgroundColor(BaseColor.WHITE);
            cellMarca.setBorderColor(BaseColor.WHITE);
            cellMarca.setBorderWidth(0f);
            cellMarca.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellMarca);

            PdfPCell cellModelo = new PdfPCell(new Paragraph(modelo));
            cellModelo.setBackgroundColor(BaseColor.WHITE);
            cellModelo.setBorderColor(BaseColor.WHITE);
            cellModelo.setBorderWidth(0f);
            cellModelo.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellModelo);


            PdfPCell cellAnio = new PdfPCell(new Paragraph(anioI));
            cellAnio.setBackgroundColor(BaseColor.WHITE);
            cellAnio.setBorderColor(BaseColor.WHITE);
            cellAnio.setBorderWidth(0f);
            cellAnio.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellAnio);


            PdfPCell cellPlaca = new PdfPCell(new Paragraph(placasI));
            cellPlaca.setBackgroundColor(BaseColor.WHITE);
            cellPlaca.setBorderColor(BaseColor.WHITE);
            cellPlaca.setBorderWidth(0f);
            cellPlaca.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellPlaca);


            PdfPCell cellVin = new PdfPCell(new Paragraph(vinI));
            cellVin.setBackgroundColor(BaseColor.WHITE);
            cellVin.setBorderColor(BaseColor.WHITE);
            cellVin.setBorderWidth(0f);
            cellVin.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellVin);


            PdfPCell cellMotor = new PdfPCell(new Paragraph(motorI));
            cellMotor.setBackgroundColor(BaseColor.WHITE);
            cellMotor.setBorderColor(BaseColor.WHITE);
            cellMotor.setBorderWidth(0f);
            cellMotor.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellMotor);


            PdfPCell cellkm = new PdfPCell(new Paragraph(kilometraje));
            cellkm.setBackgroundColor(BaseColor.WHITE);
            cellkm.setBorderColor(BaseColor.WHITE);
            cellkm.setBorderWidth(0f);
            cellkm.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellkm);


            PdfPCell cellCombustible = new PdfPCell(new Paragraph(gasolina));
            cellCombustible.setBackgroundColor(BaseColor.WHITE);
            cellCombustible.setBorderColor(BaseColor.WHITE);
            cellCombustible.setBorderWidth(0f);
            cellCombustible.setPadding(cellPadding);
            tableInfoUnidad.addCell(cellCombustible);

            document.add(tableInfoUnidad);


            //Tabla de Diagnostico

            document.add(new Paragraph(" "));
            document.add(new Paragraph("MOTIVO DE INGRESO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE)));

            PdfPTable tableDiagnostico = new PdfPTable(3);
            tableDiagnostico.setWidthPercentage(100);

            PdfPCell headerCellFechaIngreso = new PdfPCell(new Paragraph("FECHA", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellFechaIngreso.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellFechaIngreso.setPadding(cellPadding);
            headerCellFechaIngreso.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellFechaIngreso.setBorderWidth(0f);
            tableDiagnostico.addCell(headerCellFechaIngreso);

            PdfPCell headerCellDescipcion = new PdfPCell(new Paragraph("DESCRIPCION", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellDescipcion.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellDescipcion.setPadding(cellPadding);
            headerCellDescipcion.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellDescipcion.setBorderWidth(0f);
            tableDiagnostico.addCell(headerCellDescipcion);

            PdfPCell headerCellTipoServicio = new PdfPCell(new Paragraph("TIPO DE SERVICIO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellTipoServicio.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellTipoServicio.setPadding(cellPadding);
            headerCellTipoServicio.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellTipoServicio.setBorderWidth(0f);
            tableDiagnostico.addCell(headerCellTipoServicio);

            PdfPCell cellFechaIngreso = new PdfPCell(new Paragraph(fecha));
            cellFechaIngreso.setBackgroundColor(BaseColor.WHITE);
            cellFechaIngreso.setBorderColor(BaseColor.WHITE);
            cellFechaIngreso.setBorderWidth(0f);
            cellFechaIngreso.setPadding(cellPadding);
            tableDiagnostico.addCell(cellFechaIngreso);

            PdfPCell cellMotivoIngreso = new PdfPCell(new Paragraph(motivo));
            cellMotivoIngreso.setBackgroundColor(BaseColor.WHITE);
            cellMotivoIngreso.setBorderColor(BaseColor.WHITE);
            cellMotivoIngreso.setBorderWidth(0f);
            cellMotivoIngreso.setPadding(cellPadding);
            tableDiagnostico.addCell(cellMotivoIngreso);

            PdfPCell cellTipoServicio = new PdfPCell(new Paragraph(status.toUpperCase()));
            cellTipoServicio.setBackgroundColor(BaseColor.WHITE);
            cellTipoServicio.setBorderColor(BaseColor.WHITE);
            cellTipoServicio.setBorderWidth(0f);
            cellTipoServicio.setPadding(cellPadding);

            tableDiagnostico.addCell(cellTipoServicio);

            document.add(tableDiagnostico);


            //Tabla refacciones
            document.add(new Paragraph(" "));
            PdfPCell titleCell = new PdfPCell(new Phrase("REFACCIONES", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE)));
            titleCell.setBackgroundColor(BaseColor.BLACK);
            titleCell.setBorder(Rectangle.NO_BORDER); // Para eliminar el borde de la celda
            document.add(titleCell);


            PdfPTable tableRefa = new PdfPTable(5);
            tableRefa.setWidthPercentage(100);

            PdfPCell headerCellClaveRef = new PdfPCell(new Paragraph("CLAVE", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellClaveRef.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellClaveRef.setPadding(cellPadding);
            headerCellClaveRef.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellClaveRef.setBorderWidth(0f);
            tableRefa.addCell(headerCellClaveRef);

            PdfPCell headerCellCantidad = new PdfPCell(new Paragraph("CANTIDAD", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellCantidad.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellCantidad.setPadding(cellPadding);
            headerCellCantidad.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellCantidad.setBorderWidth(0f);
            tableRefa.addCell(headerCellCantidad);

            PdfPCell headerCellUnidades = new PdfPCell(new Paragraph("UNIDADES", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellUnidades.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellUnidades.setPadding(cellPadding);
            headerCellUnidades.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellCantidad.setBorderWidth(0f);
            tableRefa.addCell(headerCellUnidades);

            PdfPCell headerCellDescripcion = new PdfPCell(new Paragraph("DESCRIPCION", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellDescripcion.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellDescripcion.setPadding(cellPadding);
            headerCellDescripcion.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellCantidad.setBorderWidth(0f);
            tableRefa.addCell(headerCellDescripcion);

            PdfPCell headerCellPrecioRef = new PdfPCell(new Paragraph("PRECIO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellPrecioRef.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellPrecioRef.setPadding(cellPadding);
            headerCellPrecioRef.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellCantidad.setBorderWidth(0f);
            tableRefa.addCell(headerCellPrecioRef);



/*
            JSONArray jsonArray = new JSONArray(listaRefacciones);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String clave = jsonObject.getString("clave");
                String cantidad = jsonObject.getString("cantidad");
                String unidad = jsonObject.getString("unidad");
                String descripcion = jsonObject.getString("descripcion");
                String precio = jsonObject.getString("precio");
*/

            PdfPCell cellClave = new PdfPCell(new Paragraph("clave"));
            cellClave.setBackgroundColor(BaseColor.WHITE);
            cellClave.setBorderColor(BaseColor.WHITE);
            cellClave.setBorderWidth(0f);
            cellClave.setPadding(cellPadding);
            tableRefa.addCell(cellClave);

            PdfPCell cellCantidad = new PdfPCell(new Paragraph("cantidad"));
            cellCantidad.setBackgroundColor(BaseColor.WHITE);
            cellCantidad.setBorderColor(BaseColor.WHITE);
            cellCantidad.setBorderWidth(0f);
            cellCantidad.setPadding(cellPadding);
            tableRefa.addCell(cellCantidad);


            PdfPCell cellUnidad = new PdfPCell(new Paragraph("unidad"));
            cellUnidad.setBackgroundColor(BaseColor.WHITE);
            cellUnidad.setBorderColor(BaseColor.WHITE);
            cellUnidad.setBorderWidth(0f);
            cellUnidad.setPadding(cellPadding);
            tableRefa.addCell(cellUnidad);


            PdfPCell cellDescripcion = new PdfPCell(new Paragraph("descripcion"));
            cellDescripcion.setBackgroundColor(BaseColor.WHITE);
            cellDescripcion.setBorderColor(BaseColor.WHITE);
            cellDescripcion.setBorderWidth(0f);
            cellDescripcion.setPadding(cellPadding);
            tableRefa.addCell(cellDescripcion);


            PdfPCell cellPrecio = new PdfPCell(new Paragraph("precio"));
            cellPrecio.setBackgroundColor(BaseColor.WHITE);
            cellPrecio.setBorderColor(BaseColor.WHITE);
            cellPrecio.setBorderWidth(0f);
            cellPrecio.setPadding(cellPadding);
            tableRefa.addCell(cellPrecio);

//            }

            document.add(tableRefa);


            //Tabla de checks
            document.add(new Paragraph(" "));

            PdfPTable tableEstructuraUnidad = new PdfPTable(5);
            tableEstructuraUnidad.setWidthPercentage(90);

            PdfPCell headerCellestructuraUnidad = new PdfPCell(new Paragraph("ESTRUCTURA UNIDAD", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellestructuraUnidad.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellestructuraUnidad.setPadding(cellPadding);
            headerCellestructuraUnidad.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellestructuraUnidad.setBorderWidth(0f);
            tableEstructuraUnidad.addCell(headerCellestructuraUnidad);

            PdfPCell headerCellBueno = new PdfPCell(new Paragraph("BUENO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellBueno.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellBueno.setPadding(cellPadding);
            headerCellBueno.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellBueno.setBorderWidth(0f);
            tableEstructuraUnidad.addCell(headerCellBueno);

            PdfPCell headerCellRegular = new PdfPCell(new Paragraph("REGULAR", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellRegular.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellRegular.setPadding(cellPadding);
            headerCellRegular.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellRegular.setBorderWidth(0f);
            tableEstructuraUnidad.addCell(headerCellRegular);

            PdfPCell headerCellMalo = new PdfPCell(new Paragraph("MALO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellMalo.setBackgroundColor(BaseColor.LIGHT_GRAY); // Establecer el color de fondo a negro
            headerCellMalo.setPadding(cellPadding);
            headerCellMalo.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellMalo.setBorderWidth(0f);
            tableEstructuraUnidad.addCell(headerCellMalo);

            PdfPCell headerCellNA = new PdfPCell(new Paragraph("NO APLICA", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
            headerCellNA.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCellNA.setPadding(cellPadding);
            headerCellNA.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCellNA.setBorderWidth(0f);
            tableEstructuraUnidad.addCell(headerCellNA);


            JSONArray jsonArray = new JSONArray(listaChekList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String descripcion = jsonObject.getString("descripcion");
                String valorcheck = jsonObject.getString("valorcheck");


                PdfPCell cellCheck = new PdfPCell(new Paragraph(descripcion));
                cellCheck.setBackgroundColor(BaseColor.WHITE);
                cellCheck.setBorderColor(BaseColor.WHITE);
                cellCheck.setBorderWidth(0f);
                cellCheck.setPadding(cellPadding);
                tableEstructuraUnidad.addCell(cellCheck);


                PdfPCell cellBueno = null;
                PdfPCell cellRegular = null;
                PdfPCell cellMalo = null;
                PdfPCell cellNA = null;

                if (valorcheck.equalsIgnoreCase("R")) {
                    cellBueno = new PdfPCell(new Paragraph("-"));
                    cellRegular = new PdfPCell(new Paragraph("X"));
                    cellMalo = new PdfPCell(new Paragraph("-"));
                    cellNA = new PdfPCell(new Paragraph("-"));
                } else if (valorcheck.equalsIgnoreCase("B")) {

                    cellBueno = new PdfPCell(new Paragraph("x"));
                    cellRegular = new PdfPCell(new Paragraph("-"));
                    cellMalo = new PdfPCell(new Paragraph("-"));
                    cellNA = new PdfPCell(new Paragraph("-"));
                } else if (valorcheck.equalsIgnoreCase("NA")) {

                    cellBueno = new PdfPCell(new Paragraph("."));
                    cellRegular = new PdfPCell(new Paragraph("-"));
                    cellMalo = new PdfPCell(new Paragraph("-"));
                    cellNA = new PdfPCell(new Paragraph("X"));
                } else if (valorcheck.equalsIgnoreCase("M")) {
                    cellBueno = new PdfPCell(new Paragraph("-"));
                    cellRegular = new PdfPCell(new Paragraph("-"));
                    cellMalo = new PdfPCell(new Paragraph("X"));
                    cellNA = new PdfPCell(new Paragraph("-"));
                } else {

                    cellBueno = new PdfPCell(new Paragraph("-"));
                    cellRegular = new PdfPCell(new Paragraph("-"));
                    cellMalo = new PdfPCell(new Paragraph("-"));
                    cellNA = new PdfPCell(new Paragraph("-"));
                }


                cellBueno.setBackgroundColor(BaseColor.WHITE);
                cellBueno.setBorderColor(BaseColor.WHITE);
                cellBueno.setBorderWidth(0f);
                cellBueno.setPadding(cellPadding);
                tableEstructuraUnidad.addCell(cellBueno);


                cellRegular.setBackgroundColor(BaseColor.WHITE);
                cellRegular.setBorderColor(BaseColor.WHITE);
                cellRegular.setBorderWidth(0f);
                cellRegular.setPadding(cellPadding);
                tableEstructuraUnidad.addCell(cellRegular);


                cellMalo.setBackgroundColor(BaseColor.WHITE);
                cellMalo.setBorderColor(BaseColor.WHITE);
                cellMalo.setBorderWidth(0f);
                cellMalo.setPadding(cellPadding);
                tableEstructuraUnidad.addCell(cellMalo);


                cellNA.setBackgroundColor(BaseColor.WHITE);
                cellNA.setBorderColor(BaseColor.WHITE);
                cellNA.setBorderWidth(0f);
                cellNA.setPadding(cellPadding);
                tableEstructuraUnidad.addCell(cellNA);

            }

            document.add(tableEstructuraUnidad);


            document.close();
            Uri pdfUri = FileProvider.getUriForFile(requireContext(), "com.example.validacion.fileprovider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Enviar PDF usando:"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfPCell createGrayCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    class PageEventHandler extends PdfPageEventHelper {
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            document.newPage();
        }

    }


    private void generatePdfFromUrls(List<SlideItem> slideImagenes, File pdfDetalles) {
        PdfDocument pdfDocument = new PdfDocument();

        for (int i = 0; i < slideImagenes.size(); i++) {
            final int position = i;
            SlideItem slideItem = slideImagenes.get(i);
            String imageUrl = slideItem.getImage();

            Picasso.get().load(imageUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), position + 2).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);
                    canvas.drawBitmap(bitmap, 0, 0, paint);

                    pdfDocument.finishPage(page);

                    if (position == slideImagenes.size() - 1) {
                        pdfFileImagenes = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "images_to_pdf.pdf");
                        try {
                            pdfDocument.writeTo(new FileOutputStream(pdfFileImagenes));
                            pdfDocument.close();

                            if (pdfFileImagenes != null && pdfFileImagenes.exists()) {
                                //compartirPDFDeImagnes();
                                // PDFUtils.combinarYCompartirPDFs(context, pdfDetalles, pdfFileImagenes);


                                combinarPDFs(pdfDetalles, pdfFileImagenes, context);

                            } else {
                                Toast.makeText(context, "PDF file not found.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    Toast.makeText(requireContext(), "No se pudo cargar la imagen desde el url ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
    }

    public static void combinarPDFs(File archivoPDF1, File archivoPDF2, Context context) {
        try {
            Document documento = new Document();
            File archivoResultado = new File(context.getCacheDir(), "resultado.pdf");

            FileOutputStream fos = new FileOutputStream(archivoResultado);
            PdfCopy copiaPDF = new PdfCopy(documento, fos);
            documento.open();

            // Agregar páginas del primer archivo PDF
            PdfReader lectorPDF1 = new PdfReader(new FileInputStream(archivoPDF1));
            int numPaginas1 = lectorPDF1.getNumberOfPages();
            for (int i = 1; i <= numPaginas1; i++) {
                copiaPDF.addPage(copiaPDF.getImportedPage(lectorPDF1, i));
            }

            // Agregar páginas del segundo archivo PDF
            PdfReader lectorPDF2 = new PdfReader(new FileInputStream(archivoPDF2));
            int numPaginas2 = lectorPDF2.getNumberOfPages();
            for (int i = 1; i <= numPaginas2; i++) {
                copiaPDF.addPage(copiaPDF.getImportedPage(lectorPDF2, i));
            }

            documento.close();
            fos.close();
            lectorPDF1.close();
            lectorPDF2.close();

            // Generar URI para el archivo resultante
            String authority = "com.example.validacion.fileprovider"; // Reemplaza con tu autoridad
            Uri pdfUri = FileProvider.getUriForFile(context, authority, archivoResultado);

            // Compartir el PDF resultante
            Intent compartirIntent = new Intent();
            compartirIntent.setAction(Intent.ACTION_SEND);
            compartirIntent.setType("application/pdf");
            compartirIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            compartirIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(compartirIntent, "Compartir PDF"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPdfFormatException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }


    private void compartirPDFDeImagnes() {

        Uri pdfUri = FileProvider.getUriForFile(context,
                "com.example.validacion.fileprovider", pdfFileImagenes);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share PDF"));
    }

    File pdfFile;

    private void generarPDF(List<Mecanicos> listaMec, List<Refacciones> listRef, List<ActividadadesUnidad> listaActividade) {
        Document document = new Document();

        try {

            pdfFile = new File(requireContext().getExternalCacheDir() + File.separator + "Servicio Automotriz para " + marca + " " + modelo + ".pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
            PageEventHandler eventHandler = new PageEventHandler();
            pdfWriter.setPageEvent(eventHandler);


            document.open();

            Drawable drawable = getResources().getDrawable(R.drawable.logo);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(74, 74);
            image.setAlignment(Image.ALIGN_CENTER);

            Paragraph spaceBelowImage = new Paragraph(" ");
            spaceBelowImage.setSpacingAfter(10);

            Paragraph title = new Paragraph("Reporte de servicio de " + marca + " " + modelo);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            Paragraph spaceBelowTitle = new Paragraph(" ");
            spaceBelowTitle.setSpacingAfter(10);

            float cellPadding = 10f;
            float cellPaddingUser = 5f;

            document.add(image);
            document.add(spaceBelowImage);

            PdfPTable userInfoTable = new PdfPTable(2);
            userInfoTable.setWidthPercentage(55);

            BaseColor backgroundColor = BaseColor.LIGHT_GRAY;

            PdfPCell headerCell = new PdfPCell(new Paragraph("Información del usuario"));
            headerCell.setBackgroundColor(backgroundColor);
            headerCell.setPadding(cellPadding);
            headerCell.setColspan(2);
            userInfoTable.addCell(headerCell);

            userInfoTable.addCell("Nombre:");
            PdfPCell nameCell = new PdfPCell(new Paragraph(nombreusuario));
            nameCell.setPadding(cellPaddingUser);
            userInfoTable.addCell(nameCell);

            userInfoTable.addCell("Teléfono:");
            PdfPCell phoneCell = new PdfPCell(new Paragraph(telefonousuario));
            phoneCell.setPadding(cellPaddingUser);
            userInfoTable.addCell(phoneCell);

            userInfoTable.addCell("Marca del vehiculo:");
            PdfPCell marcaCell = new PdfPCell(new Paragraph(marca));
            marcaCell.setPadding(cellPaddingUser);
            userInfoTable.addCell(marcaCell);

            userInfoTable.addCell("Modelo del vehiculo:");
            PdfPCell modeloCell = new PdfPCell(new Paragraph(modelo));
            modeloCell.setPadding(cellPaddingUser);
            userInfoTable.addCell(modeloCell);

            document.add(userInfoTable);

            document.add(new Paragraph(" "));
            document.add(title);
            document.add(spaceBelowTitle);


            Paragraph motivodelingresovehiculo = new Paragraph("MOTIVO DEL INGRESO: " + motivo);
            document.add(new Paragraph(" "));
            document.add(motivodelingresovehiculo);
            document.add(spaceBelowTitle);


            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            PdfPCell headerCell1 = new PdfPCell(new Paragraph("Nombre del mecanico"));
            headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell1.setPadding(cellPadding);
            table.addCell(headerCell1);

            PdfPCell headerCell2 = new PdfPCell(new Paragraph("Descripción del servicio realizado"));
            headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell2.setPadding(cellPadding);
            table.addCell(headerCell2);

            Map<String, String> idPersonalToIdUsuarioMap = new HashMap<>();

            for (Mecanicos mecanicos : listaMec) {
                idPersonalToIdUsuarioMap.put(mecanicos.getIdusuario(), mecanicos.getIdusuario());
            }

            for (Mecanicos mecanicos : listaMec) {
                String nombre = mecanicos.getNombre();

                PdfPCell cell1 = new PdfPCell(new Paragraph(nombre));
                cell1.setPadding(cellPadding);
                table.addCell(cell1);

                StringBuilder cell2Content = new StringBuilder();

                for (ActividadadesUnidad actividad : listaActividadesUnidad) {
                    String idPersonalActividad = actividad.getIdpersonal();
                    String idUsuarioMecanico = idPersonalToIdUsuarioMap.get(idPersonalActividad);

                    if (idUsuarioMecanico != null && idUsuarioMecanico.equals(mecanicos.getIdusuario())) {
                        cell2Content.append("Reparacion: ").append(actividad.getObservaciones()).append("\n");
                        cell2Content.append("Hora de Inicio: ").append(actividad.getHorainicio()).append("\n");
                        cell2Content.append("Hora de Fin: ").append(actividad.getHorafin()).append("\n");
                        cell2Content.append("\n\n");
                    }
                }

                PdfPCell cell2 = new PdfPCell(new Paragraph(cell2Content.toString()));
                cell2.setPadding(cellPadding);
                table.addCell(cell2);

            }

            document.add(table);
            document.add(new Paragraph(" "));


            if (!listRef.isEmpty()) {

                PdfPTable refaccionesTable = new PdfPTable(3);
                refaccionesTable.setWidthPercentage(100);

                PdfPCell refaccionesHeaderCell1 = new PdfPCell(new Paragraph("Cantidad "));
                refaccionesHeaderCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                refaccionesHeaderCell1.setPadding(cellPadding);
                refaccionesTable.addCell(refaccionesHeaderCell1);

                PdfPCell refaccionesHeaderCell2 = new PdfPCell(new Paragraph("Detalle"));
                refaccionesHeaderCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                refaccionesHeaderCell2.setPadding(cellPadding);
                refaccionesTable.addCell(refaccionesHeaderCell2);

                PdfPCell refaccionesHeaderCell3 = new PdfPCell(new Paragraph("Precio"));
                refaccionesHeaderCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                refaccionesHeaderCell3.setPadding(cellPadding);
                refaccionesTable.addCell(refaccionesHeaderCell3);

                for (Refacciones refacciones : listRef) {
                    String cantidad = refacciones.getCantidad();
                    String descripcion = refacciones.getDescripcion();
                    String precio = refacciones.getPrecio();

                    PdfPCell refCell1 = new PdfPCell(new Paragraph(cantidad));
                    refCell1.setPadding(cellPadding);
                    refaccionesTable.addCell(refCell1);

                    PdfPCell refCell2 = new PdfPCell(new Paragraph(descripcion));
                    refCell2.setPadding(cellPadding);
                    refaccionesTable.addCell(refCell2);

                    PdfPCell refCell3 = new PdfPCell(new Paragraph(precio));
                    refCell3.setPadding(cellPadding);
                    refaccionesTable.addCell(refCell3);
                }

                document.add(refaccionesTable);

                document.add(new Paragraph(" "));
            } else {
            }


            document.close();

             compartirPDF(pdfFile);

         //   generatePdfFromUrls(slideItemsPrueba, pdfFile);

        } catch (Exception e) {
            e.printStackTrace();

            Log.d("El error del pdf es:", e.getMessage() != null ? e.getMessage() : "Mensaje de error no disponible");

        }
    }

    private void compartirPDF(File file) {

        Uri pdfUri = FileProvider.getUriForFile(requireContext(), "com.example.validacion.fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);


        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Enviar PDF usando:"));
    }


    private void CargarImagenes(String idventa) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject fotoObj = jsonArray.getJSONObject(i);
                                    String foto = fotoObj.getString("foto");
                                    String id_ser_venta = fotoObj.getString("id_ser_venta");
                                    String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/";

                                    slideItems.add(new SlideItem(fotoUrl + foto, id_ser_venta));
                                }

                                SlideAdapter slideAdapter = new SlideAdapter(slideItems, viewPager2);

                                viewPager2.setAdapter(slideAdapter);
                                viewPager2.setClipToPadding(false);
                                viewPager2.setClipChildren(false);
                                viewPager2.setOffscreenPageLimit(4);
                                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                                compositePageTransformer.addTransformer(new MarginPageTransformer(10));
                                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                    @Override
                                    public void transformPage(@NonNull View page, float position) {
                                        float r = 1 - Math.abs(position);
                                        page.setScaleY(0.85f + 0.15f);
                                    }
                                });

                                viewPager2.setPageTransformer(compositePageTransformer);
                                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);
                                        sliderHandler.removeCallbacks(sliderRunnable);
                                        sliderHandler.postDelayed(sliderRunnable, 3000);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("API Response", "Respuesta vacía");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "8");
                params.put("idventa", idventa);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(context);
        requestQueue3.add(stringRequest3);

    }

    private void CargarMecanicos(String idventa) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String foto = jsonObject.getString("foto");
                                    String nombre = jsonObject.getString("nombre");
                                    String idusuario = jsonObject.getString("idusuario");
                                    String fecha_programada = jsonObject.getString("fecha");
                                    Mecanicos mecanicos = new Mecanicos(foto, nombre, fecha_programada, idusuario);
                                    listaMecanicos.add(mecanicos);
                                }


                                if (listaMecanicos.isEmpty()) {
                                    recyclerViewMecanicos.setVisibility(View.GONE);
                                    LinearLayout LayoutNoMecanicos = requireView().findViewById(R.id.LayoutNoMecanicos);
                                    LayoutNoMecanicos.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewMecanicos.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoMecanicos = requireView().findViewById(R.id.LayoutNoMecanicos);
                                    LayoutNoMecanicos.setVisibility(View.GONE);
                                }

                                AdaptadorMecanicos adaptadorMecanicos = new AdaptadorMecanicos(listaMecanicos);
                                LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
                                recyclerViewMecanicos.setLayoutManager(layoutManager2);
                                recyclerViewMecanicos.setAdapter(adaptadorMecanicos);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("API Response", "Respuesta vacía");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "6");
                params.put("idventa", idventa);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);

    }

    private void CargarRefacciones(String idventa) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String idrefaccion = jsonObject.getString("idrefaccion");
                                    String descripcion = jsonObject.getString("descripcion");
                                    String precio = jsonObject.getString("precio");
                                    String cantidad = jsonObject.getString("cantidad");

                                    Refacciones refaccion = new Refacciones(cantidad, descripcion, precio, idrefaccion);
                                    listaRefacciones.add(refaccion);
                                }

                                if (listaRefacciones.isEmpty()) {
                                    recyclerViewRefacciones.setVisibility(View.GONE);
                                    LinearLayout LayoutNoRefacciones = requireView().findViewById(R.id.LayoutNoRefacciones);
                                    LayoutNoRefacciones.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewRefacciones.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoRefacciones = requireView().findViewById(R.id.LayoutNoRefacciones);
                                    LayoutNoRefacciones.setVisibility(View.GONE);

                                    AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(listaRefacciones);
                                    recyclerViewRefacciones.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerViewRefacciones.setAdapter(adaptadorRefacciones);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            tvRefacciones.setText("No hay Refacciones para mostrar");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "3");
                params.put("idventa", idventa);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void CargarBitacora(String idservicio) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String horainicio = jsonObject.getString("horainicio");
                                    String horafin = jsonObject.getString("horafin");
                                    String fecha = jsonObject.getString("fecha");
                                    String id_servicio = jsonObject.getString("id_servicio");
                                    String idpersonal = jsonObject.getString("idpersonal");
                                    String observaciones = jsonObject.getString("observaciones");
                                    String estatus = jsonObject.getString("estatus");
                                    String nombre = jsonObject.getString("nombre");
                                    String telefono = jsonObject.getString("telefono");
                                    String area = jsonObject.getString("area");
                                    String foto = jsonObject.getString("foto");

                                    ActividadadesUnidad actividadadesUnidad = new ActividadadesUnidad(horainicio, horafin, fecha, id_servicio, idpersonal, observaciones, estatus, nombre, telefono, area, foto);
                                    listaActividadesUnidad.add(actividadadesUnidad);
                                }

                                if (listaActividadesUnidad.isEmpty()) {
                                    recyclerViewBitacora.setVisibility(View.GONE);
                                    LinearLayout LayoutNoActividades = requireView().findViewById(R.id.LayoutNoActividades);
                                    LayoutNoActividades.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewBitacora.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoActividades = requireView().findViewById(R.id.LayoutNoActividades);
                                    LayoutNoActividades.setVisibility(View.GONE);

                                    AdaptadorActividadesUnidad adaptadorActividadesUnidad = new AdaptadorActividadesUnidad(listaActividadesUnidad);
                                    recyclerViewBitacora.setLayoutManager(new LinearLayoutManager(requireContext()));
                                    recyclerViewBitacora.setAdapter(adaptadorActividadesUnidad);
                                }

                            } catch (JSONException e) {

                                Log.e("JSON Error", "Error al analizar la respuesta JSON: " + e.getMessage());
                            }
                        } else {
                            tvBitacora.setText("No hay Actividades para esta unidad");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "26");
                params.put("idservicio", idservicio);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

/*
    private void CargarChecks(String idservicio) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            listaChecks.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listaChecks.add(jsonObject);
                            }

                            Toast.makeText(context, " Ola", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {

                            Toast.makeText(context, " Error al cargar los datos \nError:" +e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("JSON Error", "Error al analizar la respuesta JSON: " + e.getMessage());
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "11");
                params.put("idservicio", idservicio);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
*/

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

}