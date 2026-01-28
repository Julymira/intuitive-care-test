package br.com.julyana.service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import br.com.julyana.model.*;
import br.com.julyana.parser.CsvProcessor;

public class ProcessadorContabilService {

    private CsvProcessor acesso;
    private Map<String, BigDecimal> somaPorOperadora = new HashMap<>();

    public ProcessadorContabilService() {
        this.acesso = new CsvProcessor();
    }

    public void iniciar() throws Exception {
    File pasta = new File("dados_ans/"); 
    
    // 1. Carrega o cadastro de operadoras
    File fileCadop = new File(pasta, "Relatorio_cadop.csv");
    if (!fileCadop.exists()) {
        throw new FileNotFoundException("Arquivo Relatorio_cadop.csv não encontrado em dados_ans/");
    }
    
    InputStream arquivoOperadoras = new FileInputStream(fileCadop);
    Map<String, Operadora> mapaOperadoras = this.acesso.carregarOperadoras(arquivoOperadoras);

    // 2. Lista e processa os 3 trimestres
    String[] trimestres = {"1T2025.csv", "2T2025.csv", "3T2025.csv"};

    for (String nomeArquivo : trimestres) {
        File arquivoTrimestre = new File(pasta, nomeArquivo);
        if (arquivoTrimestre.exists()) {
            System.out.println("Processando e somando: " + nomeArquivo);
            processarESomar(arquivoTrimestre);
        } else {
            System.out.println("Aviso: Arquivo " + nomeArquivo + " não encontrado para processar.");
        }
    }

    // 3. Gera o arquivo final consolidado e o ZIP
    gerarRelatorioFinal(mapaOperadoras);
    }

    private void processarESomar(File arquivo) throws Exception {
        List<LancamentoContabil> lancamentos = this.acesso.processar(new FileInputStream(arquivo));
        
        for (LancamentoContabil lanc : lancamentos) {
            String registroAns = lanc.getRegAns();
            BigDecimal valor = lanc.getSaldoFinal();

            somaPorOperadora.put(registroAns, somaPorOperadora.getOrDefault(registroAns, BigDecimal.ZERO).add(valor));
        }
    }

    private void gerarRelatorioFinal(Map<String, Operadora> mapaOperadoras) throws IOException {
        File arquivoCsv = new File("consolidado_despesas.csv");
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoCsv))) {
            
            writer.println("CNPJ;RazaoSocial;ValorDespesas");

            for (Map.Entry<String, BigDecimal> entrada : somaPorOperadora.entrySet()) {
                String regAns = entrada.getKey();
                BigDecimal total = entrada.getValue();
                Operadora op = mapaOperadoras.get(regAns);

                if (op != null) {
                    writer.printf("%s;%s;%.2f%n", op.getCnpj(), op.getRazaoSocial(), total);
                }
            }
        }
        System.out.println("CSV gerado. Iniciando compactação...");
        
        compactarResultado(arquivoCsv);
    }

    private void compactarResultado(File arquivoCsv) throws IOException {
        File arquivoZip = new File("consolidado_despesas.zip");
        
        try (FileOutputStream fos = new FileOutputStream(arquivoZip);
             java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(arquivoCsv)) {
            
            java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(arquivoCsv.getName());
            zos.putNextEntry(zipEntry);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
        
        System.out.println("SUCESSO! Arquivo final gerado: " + arquivoZip.getAbsolutePath());
        
    }
    
}
