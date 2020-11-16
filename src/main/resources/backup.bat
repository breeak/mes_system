rem auther:Shuo Meng
rem date:20201030
rem ******MySQL backup start********
@echo off
forfiles /p "C:\backup" /m backup_*.sql -d -30 /c "cmd /c del /f @path"
set "Ymd=%date:~0,4%%date:~5,2%%date:~8,2%0%time:~1,1%%time:~3,2%%time:~6,2%"
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldump" --opt --single-transaction=TRUE --user=root --password=th123456,. --host=localhost --protocol=tcp --port=3306 --default-character-set=utf8 --single-transaction=TRUE --routines --events "mes_system" > "E:\MES\backup\backup_%Ymd%.sql"
@echo on
rem ******MySQL backup end********

