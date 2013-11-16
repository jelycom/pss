package cn.jely.cd.util;

public enum FieldOperation {
	eq(" = "), ne(" != "), lt(" < "), le(" <= "), gt(" > "), ge(" >= "),
	bw(" like "), bn(" not like "),//以/不以 XX开始
	ew(" like "), en(" not like "), //以/不以 XX结束
	cn(" like "), nc(" not like "),//包含/不包含 XX
	in(" in "),ni(" not in "),
	n(" is null "),nn(" is not null ");// in elements not implements
	private String operate = "";

	FieldOperation(String op) {
		this.operate = op;
	}

	public String getOperate() {
		return operate;
	}

	public static void main(String[] args) {
		for (FieldOperation fo : FieldOperation.values()) {
			System.out.println(fo+fo.operate +fo.toString());//ne != ne
		}
		System.out.println("--------------");
		System.out.println(FieldOperation.valueOf(FieldOperation.class, "en")
				.getOperate());
		System.out.println(FieldOperation.valueOf(FieldOperation.class, "en"));
	}
}
 