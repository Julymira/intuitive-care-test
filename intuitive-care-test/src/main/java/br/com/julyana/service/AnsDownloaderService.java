package br.com.julyana.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AnsDownloaderService {

    private final String URL_2025 = "https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/2025/";
    private final String PASTA_DESTINO = "dados_ans/";

    public void baixarArquivos() throws Exception {
        System.out.println("Conectando ao portal da ANS...");

        File diretorio = new File(PASTA_DESTINO);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        //Baixar o Relatorio_cadop automaticamente
        System.out.println("Baixando cadastro de operadoras atualizado...");
        String urlCadop = "https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas/Relatorio_cadop.csv";
        FileUtils.copyURLToFile(new URL(urlCadop), new File(PASTA_DESTINO + "Relatorio_cadop.csv"), 10000, 10000);

        // BUSCA PELOS TRIMESTRES
        Document doc = Jsoup.connect(URL_2025).get();
        Elements linksZips = doc.select("a[href$=.zip]");

        for (Element link : linksZips) {
            String nomeArquivo = link.text();
            String urlDownload = URL_2025 + link.attr("href");
            File arquivoZip = new File(PASTA_DESTINO + nomeArquivo);

            System.out.println("Baixando: " + nomeArquivo + "...");
            FileUtils.copyURLToFile(new URL(urlDownload), arquivoZip, 15000, 15000);

            extrair(arquivoZip);
        }
    }

    private void extrair(File arquivoZip) throws IOException {
        System.out.println("Extraindo: " + arquivoZip.getName() + "...");
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(arquivoZip))) {
            ZipEntry entry = zis.getNextEntry();
            
            while (entry != null) {
                File novoArquivo = new File(PASTA_DESTINO, entry.getName());
                
                // Cria diretórios se necessário
                if (entry.isDirectory()) {
                    novoArquivo.mkdirs();
                } else {
                    File parent = novoArquivo.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(novoArquivo))) {
                        byte[] buffer = new byte[4096];
                        int read;
                        while ((read = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        }
    }
}
