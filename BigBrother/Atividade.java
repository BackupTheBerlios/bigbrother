public class Atividade extends Caracteristicas {
   private String strNome;
   
   
   /**
   *  Método Construtor
   */
   public Atividade(String pstrNome, int pintPeso, int pintAltura, int pintInteligencia, int pintAgilidade, int pintDeterminacao, int pintConhecGerais, int pintIdade, int pintForca, int pintEsperteza, int pintSexo) {
      this.setNome         (pstrNome);
      this.setPeso         (pintPeso);
      this.setAltura       (pintAltura);
      this.setInteligencia (pintInteligencia);
      this.setAgilidade    (pintAgilidade);
      this.setDeterminacao (pintDeterminacao);
      this.setConhecGerais (pintConhecGerais);
      this.setIdade        (pintIdade);
      this.setForca        (pintForca);
      this.setEsperteza    (pintEsperteza);
      this.setSexo         (pintSexo);
   }
   
   public void setNome(String pstrNome) {
      this.strNome = pstrNome;
   }
   
   public String getNome() {
      return this.strNome;
   }
   
   public boolean Alterar(String pstrNome, int pintPeso, int pintAltura, int pintInteligencia, int pintAgilidade, int pintDeterminacao, int pintConhecGerais, int pintIdade, int pintForca, int pintEsperteza, int pintSexo) {
      this.setNome         (pstrNome);
      this.setPeso         (pintPeso);
      this.setAltura       (pintAltura);
      this.setInteligencia (pintInteligencia);
      this.setAgilidade    (pintAgilidade);
      this.setDeterminacao (pintDeterminacao);
      this.setConhecGerais (pintConhecGerais);
      this.setIdade        (pintIdade);
      this.setForca        (pintForca);
      this.setEsperteza    (pintEsperteza);
      this.setSexo         (pintSexo);
      return true;
   }
   
   public boolean Excluir() {
      return false;
   }
}
