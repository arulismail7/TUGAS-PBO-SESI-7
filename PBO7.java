/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbo7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author ARUL ISMAIL
 */
public class PBO7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int jumlahPeserta;
    int index = 0;
    Scanner input = new Scanner(System.in);

    String nim;
    String nama;
    int nilai;

    ArrayList<Mahasiswa> kumpulanPeserta = new ArrayList<>();

    System.out.print("Masukan jumlah peserta : ");
    jumlahPeserta = Integer.parseInt(input.nextLine());

    while (index != jumlahPeserta) {
      System.out.println();
      System.out.println("Peserta ke - " + (index + 1));
      System.out.print("Masukan nim peserta   : ");
      nim = input.nextLine();
      System.out.print("Masukan Nama peserta  : ");
      nama = input.nextLine();
      System.out.print("Masukan Nilai peserta : ");
      nilai = Integer.parseInt(input.nextLine());
      System.out.println();

      kumpulanPeserta.add(new Mahasiswa(nim, nama, nilai));
      index += 1;
    }

    Exam ujian = new Exam();
    ujian.setStudents(kumpulanPeserta); 
    ujian.getExamResult();
  }

}

class Exam {
    private final int passed = 0;
    private final int failed = 0;
  private float averageScore = 0;
  private ArrayList<Mahasiswa> students;
  private final ArrayList<Mahasiswa> passedStudents = new ArrayList<Mahasiswa>();
  private final ArrayList<Mahasiswa> failedStudents = new ArrayList<Mahasiswa>();

  public void setStudents(ArrayList<Mahasiswa> students) {
    this.students = students;
  }

  public Mahasiswa[] sortStudents(Mahasiswa[] students) {
    for (int i = 0; i < students.length; i++) {
      Mahasiswa temp = students[i];
      int position = i;

      while( 
        position >= 1 && 
        students[position - 1].getGrade().charAt(0) > temp.getGrade().charAt(0) 
      ) {
        students[position] = students[position - 1];
        position--;
      }

      students[position] = temp;
    }

    return students;
  }

  public Mahasiswa[][] categorizeStudentsByGrade(Mahasiswa[] arrayOfStudents) {
    int gradeIndex = 0; 
    int studentIndex = 0;
    Mahasiswa[] sortedArrayOfStudents = this.sortStudents(arrayOfStudents);

    Mahasiswa[][] groupedByGradeStudents = new Mahasiswa[5][this.students.size()];
    String pointer = sortedArrayOfStudents[0].getGrade();

    for (Mahasiswa student: sortedArrayOfStudents) {
      if (!pointer.equals(student.getGrade())) {
        pointer = student.getGrade();
        gradeIndex++;
        studentIndex = 0;
      }

      if (pointer.equals(student.getGrade())) {
        groupedByGradeStudents[gradeIndex][studentIndex] = student;
        studentIndex += 1;
      } 
    }

    return groupedByGradeStudents;
  }

  public void getSummaryOfStudents() {
    ArrayList<Mahasiswa> mergedStudents = new ArrayList<>();
    mergedStudents.addAll(this.passedStudents);
    mergedStudents.addAll(this.failedStudents);

    Mahasiswa[] arrayOfStudents = mergedStudents.toArray(
      new Mahasiswa[this.students.size()]
    ); 

    Mahasiswa[][] categorizedStudentsByGrade = this.categorizeStudentsByGrade(
      arrayOfStudents
    );

    for (Mahasiswa[] studentsByGrade: categorizedStudentsByGrade) {
      if (studentsByGrade[0] != null) {
        ArrayList<Mahasiswa> students = new ArrayList<>(
          Arrays.asList(studentsByGrade)
        );
        System.out.println(
          "Jumlah Mahasiswa dengan Nilai " + studentsByGrade[0].getGrade() 
          + " yaitu " + this.mapStudents(students)
        ); 
      }
    }
  }

  public String mapStudents(ArrayList<Mahasiswa> students) {
    String finalResult = "";
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i) != null) {
        if (i == 0) {
          finalResult += students.get(i);
        } else {
          finalResult += ", " + students.get(i);
        }
      }
    }
    return finalResult;
  }

  public void getExamResult() {
      this.students.stream().map((student) -> {
          System.out.println("NIM   : " + student.getNim());
            return student;
        }).map((student) -> {
            System.out.println("Nama  : " + student.getNama());
            return student;
        }).map((student) -> {
            System.out.println("Nilai : " + student.getNilai());
            return student;
        }).map((student) -> {
            System.out.println("Grade : " + student.getGrade());
            return student;
        }).map((student) -> {
            System.out.println("=================================");
            return student;
        }).map((student) -> {
            if (
                    student.getGrade().equals("A") ||
                    student.getGrade().equals("B") ||
                    student.getGrade().equals("C")
                    ) {
                this.passedStudents.add(student);
            } else {
                this.failedStudents.add(student);
            }     return student;
        }).forEachOrdered((student) -> {
            this.averageScore += student.getNilai();
        });

    this.averageScore = this.averageScore / this.students.size();

    System.out.println("Jumlah Mahasiswa                   : " + this.students.size());
    System.out.println(
      "Jumlah Mahasiswa yang lulus        : " + this.passedStudents.size() +
      " yaitu " + this.mapStudents(this.passedStudents)
    );
    System.out.println(
      "Jumlah Mahasiswa yang tidak lulus  : "  + this.failedStudents.size() +
      " yaitu " + this.mapStudents(this.failedStudents)
    );
    this.getSummaryOfStudents();
    System.out.println("Rata-rata nilai mahasiswa adalah   : " + this.averageScore);
  }
}

class Grade {
  protected int nilai;

  Grade(int nilai) {
    this.nilai = nilai;
  }

  public int getNilai() {
    return this.nilai;
  }

  public String getGrade() {
    if (this.nilai >= 80 && this.nilai <= 100) return "A";
    if (this.nilai >= 70 && this.nilai <= 79) return "B";
    if (this.nilai >= 60 && this.nilai <= 69) return "C";
    if (this.nilai >= 50 && this.nilai <= 59) return "D";
    if (this.nilai < 50) return "E";
    else return "Invalid Input";
  }
}

class Mahasiswa extends Grade {
  private final String nim;
  private final String nama;

  Mahasiswa(String nim, String nama, int nilai) {
    super(nilai);
    this.nim = nim;
    this.nama = nama;
  }

  public String getNim() {
    return this.nim;
  }

  public String getNama() {
    return this.nama;
  }

  @Override
  public int getNilai() {
    return this.nilai;
  }

  @Override
  public String toString() {
    return this.nama;
    }
    
}
