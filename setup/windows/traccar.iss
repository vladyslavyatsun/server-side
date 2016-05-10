[Setup]
AppName=Traccar
AppVersion=3.5
DefaultDirName={pf}\Traccar
AlwaysRestart=yes

[Dirs]
Name: "{app}\bin"
Name: "{app}\conf"
Name: "{app}\data"
Name: "{app}\lib"
Name: "{app}\logs"
Name: "{app}\web"

[Files]
Source: "..\wrapper\bin\wrapper-windows-x86-32.exe"; DestDir: "{app}\bin"; DestName: "wrapper.exe"
Source: "..\wrapper\src\bin\App.bat.in"; DestDir: "{app}\bin"; DestName: "Traccar.bat"
Source: "..\wrapper\src\bin\InstallApp-NT.bat.in"; DestDir: "{app}\bin"; DestName: "InstallTraccar-NT.bat"
Source: "..\wrapper\src\bin\UninstallApp-NT.bat.in"; DestDir: "{app}\bin"; DestName: "UninstallTraccar-NT.bat"
Source: "..\wrapper\lib\wrapper-windows-x86-32.dll"; DestDir: "{app}\lib"; DestName: "wrapper.dll"
Source: "..\wrapper\lib\wrapper.jar"; DestDir: "{app}\lib";
Source: "..\wrapper\src\conf\wrapper.conf.in"; DestDir: "{app}\conf"; DestName: "wrapper.conf"; AfterInstall: ConfigureWrapper

Source: "..\..\target\tracker-server.jar"; DestDir: "{app}"
Source: "..\..\target\lib\*"; DestDir: "{app}\lib"
Source: "..\..\database\*"; DestDir: "{app}\data"
Source: "..\..\web\*"; DestDir: "{app}\web"; Flags: recursesubdirs
Source: "traccar.xml"; DestDir: "{app}\conf"; AfterInstall: ConfigureApplication

[Run]
Filename: "{app}\bin\InstallTraccar-NT.bat"

[UninstallRun]
Filename: "{app}\bin\UninstallTraccar-NT.bat"

[Code]
function GetLocalMachine(): Integer;
begin
  if IsWin64 then
  begin
    Result := HKLM64;
  end
  else
  begin
    Result := HKEY_LOCAL_MACHINE;
  end;
end;

function InitializeSetup(): Boolean;
begin
  if RegKeyExists(GetLocalMachine(), 'SOFTWARE\JavaSoft\Java Runtime Environment') then
  begin
    Result := true;
  end
  else
  begin
    Result := false;
    MsgBox('This application requires Java Runtime Environment version 7 or later. Please download and install the JRE and run this setup again. If you have Java installed and still get this error, you need to re-install it from offline installer (for more info see https://www.traccar.org/windows/).', mbCriticalError, MB_OK);
  end;
end;

procedure ConfigureWrapper();
var
  S: String;
begin
  LoadStringFromFile(ExpandConstant(CurrentFileName), S);
  Insert('wrapper.java.classpath.2=../tracker-server.jar' + #13#10, S, Pos('wrapper.java.classpath.1', S));
  Insert(ExpandConstant('wrapper.app.parameter.2="{app}\conf\traccar.xml"') + #13#10, S, Pos('wrapper.app.parameter.1', S));
  StringChangeEx(S, 'wrapper.java.additional.1=', 'wrapper.java.additional.1=-Dfile.encoding=UTF-8', true);
  StringChangeEx(S, '<YourMainClass>', 'org.traccar.Main', true);
  StringChangeEx(S, '@app.name@', 'Traccar', true);
  StringChangeEx(S, '@app.long.name@', 'Traccar', true);
  StringChangeEx(S, '@app.description@', 'Traccar', true);
  StringChangeEx(S, 'wrapper.logfile=../logs/wrapper.log', 'wrapper.logfile=../logs/wrapper.log.YYYYMMDD' + #13#10 + 'wrapper.logfile.rollmode=DATE', true);
  SaveStringToFile(ExpandConstant(CurrentFileName), S, false);
end;

procedure ConfigureApplication();
var
  S: String;
begin
  LoadStringFromFile(ExpandConstant(CurrentFileName), S);
  StringChangeEx(S, '[WEB]', ExpandConstant('{app}\web'), true);
  StringChangeEx(S, '[LOG]', ExpandConstant('{app}\logs\tracker-server.log'), true);
  StringChangeEx(S, '[DATABASE]', ExpandConstant('{app}\data\database'), true);
  StringChangeEx(S, '[CHANGELOG]', ExpandConstant('{app}\data\changelog-master.xml'), true);
  SaveStringToFile(ExpandConstant(CurrentFileName), S, false);
end;
