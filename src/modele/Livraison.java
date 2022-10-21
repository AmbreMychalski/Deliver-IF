package modele;

import java.util.Date;

public class Livraison {
	private DemandeLivraison demandeLivraison;
	private Date date;
	
	public Livraison(DemandeLivraison demandeLivraison, Date date) {
		super();
		this.demandeLivraison = demandeLivraison;
		this.date = date;
	}

	public DemandeLivraison getDemandeLivraison() {
		return demandeLivraison;
	}

	public void setDemandeLivraison(DemandeLivraison demandeLivraison) {
		this.demandeLivraison = demandeLivraison;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
