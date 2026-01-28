package br.com.julyana;

import br.com.julyana.service.AnsDownloaderService;
import br.com.julyana.service.ProcessadorContabilService; 

public class App 
{
    public static void main( String[] args ) throws Exception {

    AnsDownloaderService downloader = new AnsDownloaderService();
    downloader.baixarArquivos();

    ProcessadorContabilService servico = new ProcessadorContabilService();
    servico.iniciar();
}
}
