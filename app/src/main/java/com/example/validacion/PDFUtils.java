package com.example.validacion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFUtils {

    public static void combinarPDFs(File archivoPDF1, File archivoPDF2, File archivoResultado) {
        try {
            Document documento = new Document();
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPdfFormatException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }


    private static void compartirPDF(Context context, File file) {
        // Obtener la URI del archivo utilizando FileProvider
        Uri pdfUri = FileProvider.getUriForFile(context, "com.example.validacion.fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);

        // Otorgar permisos de lectura al destinatario de la intención
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Iniciar la actividad para compartir
        context.startActivity(Intent.createChooser(intent, "Enviar PDF usando:"));
    }

}
