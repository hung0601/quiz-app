Hướng dẫn cài đặt:

- Đối với API server: mã nguồn được nén trong file quiz-app-api.zip, sau đây là các bước cài đặt, yêu cầu cài đặt composer, chuẩn bị CSDL bất kỳ
 + Giải nén mã nguồn
 + cd vào thư mục mã nguồn
 + Chạy lệnh: composer install
 + Lần lượt chạy:
   php artisan key:generate /
   php artisan migrate /
   php artisan db:seed /
+ Chạy:  php artisan serve

- Đối với android app: mã nguồn được nén trong file quiz-app-android.zip
  + Tải android studio
  + Mở dự án đã giải nén bằng android studio
  + Cài đặt các dependency
  + Tạo máy ảo mobile và chạy ứng dụng

- Đối với website quản lý: mã nguồng được nén trong file quiz-app-web.zip
  + Giải nén thư mục
  + cd vào thư mục chứa mã nguồn
  + Chạy lệnh:
      npm i /
      npm run dev /
