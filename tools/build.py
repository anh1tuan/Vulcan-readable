import subprocess
import shutil
import zipfile
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC_JAVA = ROOT / "src" / "main" / "java"
SRC_RESOURCES = ROOT / "src" / "main" / "resources"
BUILD_DIR = ROOT / "build"
CLASSES_DIR = BUILD_DIR / "classes"
OUTPUT_JAR = BUILD_DIR / "Vulcan-2.9.7.23-deobf.jar"
STUBS_JAR = ROOT / "tools" / "stubs.jar"

def main():
    print("[1/4] Preparing build directories...")
    if CLASSES_DIR.exists():
        shutil.rmtree(CLASSES_DIR)
    CLASSES_DIR.mkdir(parents=True, exist_ok=True)
    
    if not STUBS_JAR.exists():
        print("  Generating stubs.jar...")
        gen_script = ROOT / "tools" / "generate_all_stubs.py"
        subprocess.check_call([sys.executable, str(gen_script)])
        
    print("[2/4] Collecting sources...")
    sources = list(SRC_JAVA.rglob("*.java"))
    print(f"  Found {len(sources)} Java source files.")
    sources_file = BUILD_DIR / "sources.txt"
    sources_file.write_text("\n".join(str(p) for p in sources), encoding="utf-8")
    
    print("[3/4] Compiling with javac (target: Java 8)...")
    cmd = [
        "javac",
        "-Xmaxerrs", "500",
        "-source", "1.8",
        "-target", "1.8",
        "-cp", str(STUBS_JAR),
        "-d", str(CLASSES_DIR),
        "-encoding", "UTF-8",
        f"@{sources_file}"
    ]
    res = subprocess.run(cmd, capture_output=True, text=True)
    if res.returncode != 0:
        print(f"Compilation FAILED with exit code {res.returncode}:")
        print(res.stderr)
        sys.exit(1)
        
    classes = list(CLASSES_DIR.rglob("*.class"))
    print(f"  Successfully compiled {len(classes)} class files (0 errors).")
    
    print("[4/4] Packaging output JAR...")
    if OUTPUT_JAR.exists():
        OUTPUT_JAR.unlink()
        
    with zipfile.ZipFile(OUTPUT_JAR, "w", zipfile.ZIP_DEFLATED) as z:
        # Add compiled classes
        for cls in classes:
            arcname = cls.relative_to(CLASSES_DIR).as_posix()
            z.write(cls, arcname)
            
        # Add resources
        if SRC_RESOURCES.exists():
            for res_file in SRC_RESOURCES.rglob("*"):
                if res_file.is_file():
                    arcname = res_file.relative_to(SRC_RESOURCES).as_posix()
                    z.write(res_file, arcname)
                    
    print(f"[SUCCESS] Built: {OUTPUT_JAR} ({OUTPUT_JAR.stat().st_size} bytes)")

if __name__ == "__main__":
    main()
