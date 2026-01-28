package br.com.julyana.model;

public class Operadora {
    
    private String cnpj;
    private String razaoSocial;
    private String regAns;

    public Operadora() {}

    public String getCnpj() { 
        return cnpj; 
    }

    public void setCnpj(String cnpj) { 
        this.cnpj = cnpj; 
    }

    public String getRazaoSocial() { 
        return razaoSocial; 
    }

    public void setRazaoSocial(String razaoSocial) { 
        this.razaoSocial = razaoSocial; 
    }

    public String getRegAns() { 
        return regAns; 
    }

    public void setRegAns(String regAns) { 
        this.regAns = regAns; 
    }
}
