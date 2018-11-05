package lists.playerList;

public enum ListNames {
	WhiteList("WhiteList.txt"), NoobList("NoobList.txt"), BlackList("BlackList.txt");

	private String fileName;

	private ListNames(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
