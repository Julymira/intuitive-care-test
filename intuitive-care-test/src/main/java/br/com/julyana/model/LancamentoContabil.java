package br.com.julyana.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentoContabil {

    private LocalDate data;          
    private String regAns;           
    private String cdContaContabil;  
    private String descricao;        
    private BigDecimal saldoFinal;   

    public LancamentoContabil() {}

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getRegAns() { return regAns; }
    public void setRegAns(String regAns) { this.regAns = regAns; }

    public String getCdContaContabil() { return cdContaContabil; }
    public void setCdContaContabil(String cdContaContabil) { this.cdContaContabil = cdContaContabil; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }
    
    public boolean isDespesaEvento() {
        if (cdContaContabil == null) return false;
        
        boolean isClasseDespesa = cdContaContabil.startsWith("4");
        
        String descUpper = (descricao != null) ? descricao.toUpperCase() : "";
        boolean contemTermosChave = descUpper.contains("EVENTOS") 
                                 || descUpper.contains("SINISTROS")
                                 || descUpper.contains("ASSISTENCIAIS");

        return isClasseDespesa && contemTermosChave;
    }
}
