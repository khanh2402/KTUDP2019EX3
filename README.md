Hi !
3. [mã câu hỏi: 102] Giao tiếp với server tại cổng 1109 theo kịch bản:
Trong đó, đối tượng trao đổi là thể hiện của lớp Student được mô tả như sau:
 Tên đầy đủ lớp: UDP.Student
 Các thuộc tính: id int,code String, gpa float, gpaLetter String
 Hàm khởi tạo:
o public Student(int id, String code, float gpa, String gpaLetter)
o public Student(String code)
 Trường dữ liệu: private static final long serialVersionUID = 20151107
Thực hiện:
a. Gửi thông điệp là một đối tượng của lớp student với thông tin duy nhất được thiết lập là
code bằng mã sinh viên của bạn
b. Nhận một đối tượng là thể hiện của lớp Student từ server với các thông tin được thiết
lập là id, code, gpa
c. Thực hiện chuyển đổi điểm gpa của đối tượng nhận được sang dạng chữ và gán cho
gpaLetter sau đó gửi đối tượng lên server.
Nguyên tắc chuyển đổi: 3.7 – 4 -> A; 3.0 – 3.7 -> B; 2.0 – 3.0 -> C; 1.0 – 2.0 -> D;
0 – 1.0 -> F