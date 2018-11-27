package pkgGame;

import java.security.SecureRandom;
import java.util.Random;

import pkgHelper.LatinSquare;

public class Sudoku extends LatinSquare {

	private int iSize;
	private int iSqrtSize;

	
	private class Cell {
		private int iRow;
		private int iCol;
		private ArrayList<Integer> lstValidValues = new ArrayList<Integer>();

		public Cell(int iRow, int iCol) {
			super();
			this.iRow = iRow;
			this.iCol = iCol;
		}

		public int getiRow() {
			return iRow;
		}

		public int getiCol() {
			return iCol;
		}

		@Override
		public int hashCode() {
			return Objects.hash(iRow, iCol);
		}

		@Override
		public boolean equals(Object o1) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Cell)) {
				return false;
			}
			Cell c1 = (Cell) o1;
			return iCol == c.iCol && iRow == c.iRow;

		}


		public ArrayList<Integer> getLstValidValues() {
			return lstValidValues;
		}


		public void setlstValidValues(HashSet<Integer> hsValidValues) {
			lstValidValues = new ArrayList<Integer>(hsValidValues);
		}

		public void ShuffleValidValues() {
			Collections.shuffle(lstValidValues);
		}
		
	public Sudoku(int iSize) throws Exception {

		this.iSize = iSize;

		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}

		int[][] puzzle = new int[iSize][iSize];
		super.setLatinSquare(puzzle);
		FillDiagonalRegions();
		SetCells();		
		fillRemaining(this.cells.get(Objects.hash(0, iSqrtSize)));
	}
	
	public Cell GetNextCell(Cell c) {
		int iCol = c.getiCol() + 1;
		int iRow = c.getiRow();
		int iSqrtSize = (int) Math.sqrt(iSize);

		if (iCol >= iSize && iRow < iSize - 1) {
			iRow = iRow + 1;
			iCol = 0;
		}
		if (iRow >= iSize && iCol >= iSize)
		{
			return null;
		}
		if (iRow < iSqrtSize) {
			if (iCol < iSqrtSize) {
				iCol = iSqrtSize;
			}
		}
		else if (iRow < iSize - iSqrtSize) {
			if (iCol == (int) (iRow / iSqrtSize) * iSqrtSize)
				iCol = iCol + iSqrtSize;
		} 
		else {
			if (iCol == iSize - iSqrtSize) {
				iRow = iRow + 1;
				iCol = 0;
				if (iRow >= iSize) {
					return null;
				}
			}
		}

		return (Cell)cells.get(Objects.hash(iRow,iCol));		

	}

	public Sudoku(int[][] puzzle) throws Exception {
		super(puzzle);
		this.iSize = puzzle.length;
		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}

	}
	
	private void SetCells() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				Cell c1 = new Cell(row,col);
				c1.setlstValidValues(getAllValidCellValues(col, row));
				c1.ShuffleValidValues();
				cells.put(c1.hashCode(), c1);
			}
		}
	}

	private void ShowAvailableValues() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {

				Cell c1 = cells.get(Objects.hash(row, col));
				for (Integer inte: c1.getLstValidValues())
				{
					System.out.print(inte + " ");
				}				
				System.out.println("");
			}
		}
	}

	private HashSet<Integer> getAllValidCellValues(int iCol, int iRow) {
		HashSet<Integer> cellRange = new HashSet<Integer>();
		for(int x = 0; x < size; x++) {
			hsCellRange.add(x + 1);
		}
		HashSet<Integer> usedValues = new HashSet<Integer>();
		Collections.addAll(usedValues, Arrays.stream(super.getRow(row)).boxed().toArray(Integer[]::new));
		Collections.addAll(usedValues, Arrays.stream(super.getColumn(col)).boxed().toArray(Integer[]::new));
		Collections.addAll(usedValues, Arrays.stream(this.getRegion(col, row)).boxed().toArray(Integer[]::new));

		cellRange.removeAll(hsUsedValues);
		return cellRange;
	}

	@Override
	public boolean hasDuplicates() {
		if(super.hasDuplicates())
			return true;

		for(int x = 0; x < this.getPuzzle().length; x++) {
			if (super.hasDuplicates(getRegion(x))) {
				super.AddPuzzleViolation(new PuzzleViolation(ePuzzleViolation.DupRegion, x));
			}
		}

		return (super.getPV().size() > 0);
	}
	
	public boolean isValidValue(Cell c1, int value) {
		return this.isValidValue(c1.getiRow(), c.getiCol(), value);
	}
	
	public boolean isValidValue(int row, int col, int value) {
		if (doesElementExist(super.getRow(row), value)) {
			return false;
		}
		if (doesElementExist(super.getColumn(col), value)) {
			return false;
		}
		if (doesElementExist(this.getRegion(col, row), value)) {
			return false;
		}

		return true;
	}
	
	public int[][] getPuzzle() {
		return super.getLatinSquare();
	}

	public int getRegionNbr(int iCol, int iRow) {

		int i = (iCol / iSqrtSize) + ((iRow / iSqrtSize) * iSqrtSize);

		return i;
	}

	public int[] getRegion(int iCol, int iRow) {
		int i = (col/sqrtSize) + ((row / sqrtSize)*sqrtSize);
		return getRegion(i);
	}

	public int[] getRegion(int r) {

		int[] reg = new int[super.getLatinSquare().length];

		int i = (r % iSqrtSize) * iSqrtSize;
		int j = (r / iSqrtSize) * iSqrtSize;
		int iMax = i + iSqrtSize;
		int jMax = j + iSqrtSize;
		int iCnt = 0;

		for (; j < jMax; j++) {
			for (i = (r % iSqrtSize) * iSqrtSize; i < iMax; i++) {
				reg[iCnt++] = super.getLatinSquare()[j][i];
			}
		}

		return reg;
	}

	public boolean isPartialSudoku() {
		super.setbIgnoreZero(true);
		super.ClearPuzzleViolation();
		if(hasDuplicates()) {
			return false;
		}
		if(!ContainsZero()) {
			super.AddPuzzleViolation(new PuzzleViolation(ePuzzleViolation.MissingZero, -1));
			return false;
		}
		return true;
	}

	public boolean isSudoku() {
		this.setbIgnoreZero(false);
		super.ClearPuzzleViolation();
		if(hasDuplicates()) {
			return false;
		}
		if(!super.isLatinSquare()) {
			return false;
		}
		for(int x = 1; x < super.getLatinSquare().length; x++) {
			if (!hasAllValues(getRow(0), getRegion(x))) {
				return false;
			}
		}

		if(ContainsZero()) {
			return false;
		}

		return true;
	}

	public void PrintPuzzle() {
		for (int i = 0; i < this.getPuzzle().length; i++) {
			System.out.println("");
			for (int j = 0; j < this.getPuzzle().length; j++) {
				System.out.print(this.getPuzzle()[i][j]);
				if ((j + 1) % iSqrtSize == 0)
					System.out.print(" ");
			}
			if ((i + 1) % iSqrtSize == 0)
				System.out.println(" ");

		}
		System.out.println("");
	}

	private void FillDiagonalRegions() {

		for (int i = 0; i < iSize; i = i + iSqrtSize) {
			SetRegion(getRegionNbr(i, i));
			ShuffleRegion(getRegionNbr(i, i));
		}
	}
	
	private boolean fillRemaining(Cell c1) {
		if (c1 == null)
			return true;

		for(int n: c1.getLstValidValues())
		{
			if (isValidValue(c1, n)) {
				this.getPuzzle()[c1.getiRow()][c.getiCol()] = n;
									
				if (fillRemaining(c1.GetNextCell(c1)))
					return true;
				this.getPuzzle()[c1.getiRow()][c1.getiCol()] = 0;
			}
		}
		return false;
	}

	private void SetRegion(int r) {
		int iValue = 0;

		iValue = 1;
		for (int i = (r / iSqrtSize) * iSqrtSize; i < ((r / iSqrtSize) * iSqrtSize) + iSqrtSize; i++) {
			for (int j = (r % iSqrtSize) * iSqrtSize; j < ((r % iSqrtSize) * iSqrtSize) + iSqrtSize; j++) {
				this.getPuzzle()[i][j] = iValue++;
			}
		}
	}

	private void shuffleArray(int[] ar) {
		Random rand = new SecureRandom();
		for(int x = ar.length - 1; x > 0; x--) {
			int index = rand.nextInt(x + 1);
			int num = ar[index];
			ar[index] = ar[x];
			ar[x] = num;
		}
	}
	
	private void ShuffleRegion(int r) {
		int[] region = getRegion(r);
		shuffleArray(region);
		int iCnt = 0;
		for (int i = (r / iSqrtSize) * iSqrtSize; i < ((r / iSqrtSize) * iSqrtSize) + iSqrtSize; i++) {
			for (int j = (r % iSqrtSize) * iSqrtSize; j < ((r % iSqrtSize) * iSqrtSize) + iSqrtSize; j++) {
				this.getPuzzle()[i][j] = region[iCnt++];
			}
		}
	}

	private void shuffleArray(int[] ar) {

		Random rand = new SecureRandom();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rand.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
}
