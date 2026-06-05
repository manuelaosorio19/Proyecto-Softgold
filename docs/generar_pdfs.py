"""
Generador de PDFs profesionales para la documentacion de SoftGold.
Convierte los tres manuales Markdown a PDF con formato empresarial.
Requiere: pip install fpdf2
"""

import os
import re
from fpdf import FPDF
from fpdf.enums import XPos, YPos

# ---------------------------------------------------------------------------
# Configuracion general
# ---------------------------------------------------------------------------
DOCS_DIR = os.path.dirname(os.path.abspath(__file__))
OUTPUT_DIR = DOCS_DIR

FONT_PATH = r"C:\Windows\Fonts"
FONT_REGULAR = os.path.join(FONT_PATH, "arial.ttf")
FONT_BOLD    = os.path.join(FONT_PATH, "arialbd.ttf")
FONT_ITALIC  = os.path.join(FONT_PATH, "ariali.ttf")
FONT_BOLDITALIC = os.path.join(FONT_PATH, "arialbi.ttf")

COLOR_PRIMARY   = (0, 100, 60)      # verde oscuro corporativo
COLOR_SECONDARY = (30, 55, 90)      # azul marino
COLOR_HEADER_BG = (20, 28, 42)      # sidebar dark (--sg-sidebar-bg)
COLOR_LIGHT_BG  = (240, 245, 250)   # fondo claro para tablas
COLOR_TEXT      = (30, 30, 30)
COLOR_GRAY      = (100, 100, 100)
COLOR_WHITE     = (255, 255, 255)
COLOR_CODE_BG   = (245, 245, 245)
COLOR_WARN_BG   = (255, 248, 220)
COLOR_WARN_LINE = (230, 160, 0)


# ---------------------------------------------------------------------------
# Clase PDF personalizada
# ---------------------------------------------------------------------------
class SoftGoldPDF(FPDF):
    """PDF con cabecera, pie de pagina y estilos corporativos SoftGold."""

    def __init__(self, title: str, subtitle: str):
        super().__init__(orientation="P", unit="mm", format="A4")
        self.doc_title   = title
        self.doc_subtitle = subtitle
        self.set_auto_page_break(auto=True, margin=20)
        self.set_margins(left=20, top=18, right=20)

        # Registrar fuentes Unicode
        self.add_font("Arial", style="",  fname=FONT_REGULAR)
        self.add_font("Arial", style="B", fname=FONT_BOLD)
        self.add_font("Arial", style="I", fname=FONT_ITALIC)
        self.add_font("Arial", style="BI",fname=FONT_BOLDITALIC)

    # ------------------------------------------------------------------
    # Cabecera de pagina
    # ------------------------------------------------------------------
    def header(self):
        if self.page_no() == 1:
            return
        # Linea de color primario
        self.set_draw_color(*COLOR_PRIMARY)
        self.set_line_width(0.8)
        self.line(20, 10, 190, 10)
        # Titulo del documento (izquierda)
        self.set_font("Arial", "B", 8)
        self.set_text_color(*COLOR_SECONDARY)
        self.set_xy(20, 12)
        self.cell(120, 5, self.doc_title, align="L")
        # "SoftGold" (derecha)
        self.set_xy(140, 12)
        self.set_text_color(*COLOR_PRIMARY)
        self.cell(50, 5, "SoftGold v1.0", align="R")
        self.ln(4)

    # ------------------------------------------------------------------
    # Pie de pagina
    # ------------------------------------------------------------------
    def footer(self):
        if self.page_no() == 1:
            return
        self.set_y(-14)
        self.set_draw_color(*COLOR_PRIMARY)
        self.set_line_width(0.4)
        self.line(20, self.get_y(), 190, self.get_y())
        self.set_y(-12)
        self.set_font("Arial", "I", 8)
        self.set_text_color(*COLOR_GRAY)
        self.cell(0, 5, f"Pagina {self.page_no()} - Uso interno confidencial", align="C")

    # ------------------------------------------------------------------
    # Portada
    # ------------------------------------------------------------------
    def cover_page(self, lines_meta: list[str]):
        self.add_page()
        # Fondo oscuro superior
        self.set_fill_color(*COLOR_HEADER_BG)
        self.rect(0, 0, 210, 80, style="F")
        # Titulo grande
        self.set_text_color(*COLOR_WHITE)
        self.set_font("Arial", "B", 28)
        self.set_xy(0, 18)
        self.cell(210, 14, "SOFTGOLD", align="C")
        # Subtitulo (nombre del documento)
        self.set_font("Arial", "B", 16)
        self.set_xy(0, 36)
        self.multi_cell(210, 9, self.doc_subtitle, align="C")
        # Linea verde
        self.set_draw_color(*COLOR_PRIMARY)
        self.set_line_width(1.5)
        self.line(30, 74, 180, 74)
        # Cuerpo de portada
        self.set_text_color(*COLOR_TEXT)
        self.set_xy(30, 95)
        self.set_font("Arial", "B", 12)
        self.cell(0, 8, "Sistema de Gestion Integral Minera", ln=True)
        self.set_font("Arial", "", 10)
        self.ln(5)
        for line in lines_meta:
            self.set_x(30)
            self.cell(0, 7, line, ln=True)
        # Marco inferior
        self.set_fill_color(*COLOR_PRIMARY)
        self.rect(0, 267, 210, 30, style="F")
        self.set_text_color(*COLOR_WHITE)
        self.set_font("Arial", "I", 9)
        self.set_xy(0, 274)
        self.cell(210, 7, "Universidad Autonoma de Manizales  |  Ingenieria de Software II  |  2026", align="C")

    # ------------------------------------------------------------------
    # Helpers de estilo
    # ------------------------------------------------------------------
    def h1(self, text: str):
        """Titulo principal de seccion."""
        self.ln(5)
        # Fondo de color para H1
        x, y = self.get_x(), self.get_y()
        self.set_fill_color(*COLOR_SECONDARY)
        self.rect(18, y, 174, 11, style="F")
        self.set_text_color(*COLOR_WHITE)
        self.set_font("Arial", "B", 13)
        self.set_xy(22, y + 1.5)
        self.cell(0, 8, self._truncate(text, 90), ln=True)
        self.set_text_color(*COLOR_TEXT)
        self.ln(2)

    def h2(self, text: str):
        """Subtitulo de subseccion."""
        self.ln(3)
        self.set_draw_color(*COLOR_PRIMARY)
        self.set_line_width(0.5)
        y = self.get_y()
        self.line(20, y, 190, y)
        self.set_text_color(*COLOR_SECONDARY)
        self.set_font("Arial", "B", 11)
        self.cell(0, 8, self._truncate(text, 95), ln=True)
        self.set_text_color(*COLOR_TEXT)

    def h3(self, text: str):
        """Titulo de tercer nivel."""
        self.ln(2)
        self.set_text_color(*COLOR_PRIMARY)
        self.set_font("Arial", "B", 10)
        self.cell(0, 7, self._truncate(text, 95), ln=True)
        self.set_text_color(*COLOR_TEXT)

    def body(self, text: str):
        """Parrafo de texto normal."""
        self.set_x(self.l_margin)
        self.set_font("Arial", "", 9.5)
        self.set_text_color(*COLOR_TEXT)
        text = self._break_long_tokens(text)
        self.multi_cell(0, 5.5, text)
        self.ln(1)

    @staticmethod
    def _break_long_tokens(text: str, max_token: int = 55) -> str:
        """Divide tokens sin espacios que superan max_token caracteres."""
        words = text.split(" ")
        result = []
        for w in words:
            if len(w) > max_token:
                # Partir el token cada max_token caracteres
                parts = [w[i:i+max_token] for i in range(0, len(w), max_token)]
                result.append(" ".join(parts))
            else:
                result.append(w)
        return " ".join(result)

    def bullet(self, text: str, level: int = 0):
        """Elemento de lista con viñeta."""
        indent = 5 + level * 8
        self.set_font("Arial", "", 9.5)
        self.set_text_color(*COLOR_TEXT)
        bullet_char = "-" if level == 0 else "o"
        self.set_x(self.l_margin + indent)
        self.cell(6, 5.5, bullet_char)
        avail_w = self.epw - indent - 6
        self.multi_cell(avail_w, 5.5, self._break_long_tokens(text.strip()))

    def numbered(self, text: str, num: int):
        """Elemento de lista numerada."""
        self.set_font("Arial", "", 9.5)
        self.set_x(self.l_margin)
        self.cell(8, 5.5, f"{num}.")
        self.set_x(self.l_margin + 8)
        self.multi_cell(self.epw - 8, 5.5, self._break_long_tokens(text.strip()))

    def code_block(self, lines: list[str]):
        """Bloque de codigo."""
        self.ln(1)
        y_start = self.get_y()
        # Calcular altura
        line_h = 4.5
        height = len(lines) * line_h + 4
        self.set_fill_color(*COLOR_CODE_BG)
        self.rect(18, y_start, 174, height, style="F")
        self.set_draw_color(180, 180, 180)
        self.rect(18, y_start, 174, height)
        self.set_draw_color(*COLOR_PRIMARY)
        self.set_line_width(0.8)
        self.line(18, y_start, 18, y_start + height)
        self.set_line_width(0.2)
        self.set_draw_color(*COLOR_TEXT)
        self.set_text_color(40, 40, 120)
        self.set_font("Arial", "", 8)
        self.set_xy(22, y_start + 2)
        for line in lines:
            self.set_x(22)
            self.cell(0, line_h, line[:110], ln=True)
        self.ln(2)
        self.set_text_color(*COLOR_TEXT)

    def note_box(self, text: str, kind: str = "NOTE"):
        """Caja de nota o advertencia."""
        self.ln(2)
        y = self.get_y()
        color_line = COLOR_WARN_LINE if kind == "WARN" else COLOR_PRIMARY
        color_bg   = COLOR_WARN_BG   if kind == "WARN" else COLOR_LIGHT_BG
        label = "ADVERTENCIA" if kind == "WARN" else "NOTA"
        self.set_fill_color(*color_bg)
        self.set_draw_color(*color_line)
        self.set_line_width(1.2)
        # Fondo
        self.rect(18, y, 174, 14, style="F")
        # Linea lateral
        self.line(18, y, 18, y + 14)
        # Texto
        self.set_font("Arial", "B", 9)
        self.set_text_color(*color_line)
        self.set_xy(23, y + 2)
        self.cell(30, 5, f">> {label}:")
        self.set_font("Arial", "", 9)
        self.set_text_color(*COLOR_TEXT)
        self.set_xy(55, y + 2)
        self.multi_cell(130, 5, text.strip())
        self.ln(4)
        self.set_line_width(0.2)

    def horizontal_rule(self):
        self.ln(2)
        self.set_draw_color(*COLOR_GRAY)
        self.set_line_width(0.2)
        y = self.get_y()
        self.line(20, y, 190, y)
        self.ln(3)

    def table_row(self, cells: list[str], widths: list[float],
                  header: bool = False, zebra: bool = False):
        """Fila de tabla con soporte de colores alternados."""
        if header:
            self.set_fill_color(*COLOR_SECONDARY)
            self.set_text_color(*COLOR_WHITE)
            self.set_font("Arial", "B", 8.5)
        elif zebra:
            self.set_fill_color(*COLOR_LIGHT_BG)
            self.set_text_color(*COLOR_TEXT)
            self.set_font("Arial", "", 8.5)
        else:
            self.set_fill_color(255, 255, 255)
            self.set_text_color(*COLOR_TEXT)
            self.set_font("Arial", "", 8.5)

        # Calcular altura maxima de la fila
        row_h = 6.5
        x0 = self.get_x()
        y0 = self.get_y()
        for i, (cell, w) in enumerate(zip(cells, widths)):
            self.set_xy(x0 + sum(widths[:i]), y0)
            self.cell(w, row_h, self._truncate(str(cell), int(w * 1.4)),
                      border=1, fill=True)
        self.ln(row_h)

    # ------------------------------------------------------------------
    # Utilidades
    # ------------------------------------------------------------------
    @staticmethod
    def _truncate(text: str, max_len: int) -> str:
        text = text.replace("​", "")  # quitar zero-width spaces
        return text[:max_len - 1] + "…" if len(text) > max_len else text


# ---------------------------------------------------------------------------
# Parser de Markdown simplificado
# ---------------------------------------------------------------------------
class MarkdownParser:
    """
    Parsea un subconjunto de Markdown y llama los metodos del PDF.
    Soporta: H1-H3, listas -, listas numeradas, bloques de codigo,
    tablas, lineas horizontales, cajas NOTE/WARN, parrafos.
    """

    def __init__(self, pdf: SoftGoldPDF):
        self.pdf = pdf

    def parse(self, text: str):
        lines = text.split("\n")
        i = 0
        code_buf: list[str] = []
        in_code = False
        table_buf: list[str] = []
        in_table = False
        num_counter = 0

        while i < len(lines):
            raw = lines[i]
            stripped = raw.strip()

            # ---- Bloque de codigo ----
            if stripped.startswith("```"):
                if in_code:
                    self.pdf.code_block(code_buf)
                    code_buf = []
                    in_code = False
                else:
                    in_code = True
                    # Vaciar tabla si habia
                    if in_table:
                        self._flush_table(table_buf)
                        table_buf = []
                        in_table = False
                i += 1
                continue

            if in_code:
                code_buf.append(raw)
                i += 1
                continue

            # ---- Tabla (detectar por | ) ----
            if "|" in stripped and stripped.startswith("|"):
                if not in_table:
                    in_table = True
                    table_buf = []
                table_buf.append(stripped)
                i += 1
                continue
            else:
                if in_table:
                    self._flush_table(table_buf)
                    table_buf = []
                    in_table = False

            # ---- Linea horizontal ----
            if re.match(r"^---+$", stripped):
                self.pdf.horizontal_rule()
                num_counter = 0
                i += 1
                continue

            # ---- Headings ----
            if stripped.startswith("### "):
                self.pdf.h3(self._clean(stripped[4:]))
                num_counter = 0
                i += 1
                continue
            if stripped.startswith("## "):
                self.pdf.h2(self._clean(stripped[3:]))
                num_counter = 0
                i += 1
                continue
            if stripped.startswith("# "):
                # Saltamos el H1 de portada (ya en cover_page)
                self.pdf.h1(self._clean(stripped[2:]))
                num_counter = 0
                i += 1
                continue

            # ---- Notas / Advertencias ----
            if stripped.startswith("> **ADVERTENCIA:**") or stripped.startswith("> **IMPORTANTE:**"):
                text_note = re.sub(r"^> \*\*[^*]+\*\*:?\s*", "", stripped)
                self.pdf.note_box(self._clean(text_note), kind="WARN")
                i += 1
                continue
            if stripped.startswith("> **NOTA:**") or stripped.startswith("> **RECOMENDACION:**"):
                text_note = re.sub(r"^> \*\*[^*]+\*\*:?\s*", "", stripped)
                self.pdf.note_box(self._clean(text_note), kind="NOTE")
                i += 1
                continue
            if stripped.startswith(">"):
                text_note = stripped.lstrip("> ").strip()
                self.pdf.note_box(self._clean(text_note), kind="NOTE")
                i += 1
                continue

            # ---- Marcadores de captura ----
            if "CAPTURA DE PANTALLA" in stripped:
                # Linea de indicacion de imagen
                self.pdf.set_fill_color(230, 240, 255)
                self.pdf.rect(20, self.pdf.get_y(), 170, 20, style="F")
                self.pdf.set_draw_color(150, 150, 200)
                self.pdf.set_line_width(0.4)
                self.pdf.rect(20, self.pdf.get_y(), 170, 20)
                self.pdf.set_font("Arial", "I", 9)
                self.pdf.set_text_color(80, 80, 150)
                self.pdf.set_x(22)
                label = re.sub(r"[-\[\]]", "", stripped).strip()
                self.pdf.cell(0, 20, f"[Imagen: {label}]", align="C", ln=True)
                self.pdf.set_text_color(*COLOR_TEXT)
                self.pdf.ln(2)
                i += 1
                continue

            # ---- Listas numeradas ----
            nm = re.match(r"^(\d+)\.\s+(.*)", stripped)
            if nm:
                num_counter += 1
                self.pdf.numbered(self._clean(nm.group(2)), num_counter)
                i += 1
                continue

            # ---- Listas con - o * ----
            bm = re.match(r"^([-*])\s+(.*)", stripped)
            if bm:
                level = 0 if not raw.startswith("  ") else 1
                self.pdf.bullet(self._clean(bm.group(2)), level=level)
                i += 1
                continue

            # ---- Parrafos normales ----
            if stripped:
                num_counter = 0
                self.pdf.body(self._clean(stripped))
                i += 1
                continue

            # ---- Linea en blanco ----
            self.pdf.ln(1)
            i += 1

        # Vaciar buffers
        if in_code:
            self.pdf.code_block(code_buf)
        if in_table:
            self._flush_table(table_buf)

    def _flush_table(self, rows: list[str]):
        """Renderiza una tabla Markdown."""
        if not rows:
            return
        # Filtrar linea separadora |---|---|
        data_rows = [r for r in rows if not re.match(r"^\|[\s\-|:]+\|?$", r)]
        if not data_rows:
            return

        parsed = []
        for row in data_rows:
            cells = [c.strip() for c in row.strip("|").split("|")]
            parsed.append(cells)

        if not parsed:
            return

        max_cols = max(len(r) for r in parsed)
        # Asegurar todas las filas tienen el mismo numero de columnas
        for row in parsed:
            while len(row) < max_cols:
                row.append("")

        # Calcular anchos de columna proporcionales
        available = 170.0
        col_w = [available / max_cols] * max_cols

        self.pdf.ln(2)
        for idx, row in enumerate(parsed):
            zebra = (idx % 2 == 0) and idx > 0
            self.pdf.table_row(row, col_w, header=(idx == 0), zebra=zebra)
        self.pdf.ln(3)

    @staticmethod
    def _clean(text: str) -> str:
        """Elimina formato Markdown inline y caracteres no soportados."""
        # Bold + italic combinado
        text = re.sub(r"\*\*\*(.+?)\*\*\*", r"\1", text)
        # Bold
        text = re.sub(r"\*\*(.+?)\*\*", r"\1", text)
        # Italic con *
        text = re.sub(r"\*(.+?)\*", r"\1", text)
        # Italic con _
        text = re.sub(r"_(.+?)_", r"\1", text)
        # Inline code
        text = re.sub(r"`([^`]+)`", r"\1", text)
        # Links [texto](url)
        text = re.sub(r"\[([^\]]+)\]\([^\)]+\)", r"\1", text)
        # Eliminar backtick sueltos
        text = text.replace("`", "")
        # Reemplazar caracteres especiales no soportados por Arial
        replacements = {
            "☐": "[ ]",  # ballot box
            "☑": "[x]",  # ballot box checked
            "☒": "[X]",  # ballot box with X
            "✔": "[OK]",  # heavy check mark
            "✘": "[X]",   # heavy x
            "→": "->",   # arrow right
            "←": "<-",   # arrow left
            "✔": "OK",   # check mark
            "■": "-",    # black square
            "●": "*",    # black circle
            "≥": ">=",   # greater or equal
            "≤": "<=",   # less or equal
            "≠": "!=",   # not equal
            "×": "x",    # multiplication sign
            "…": "...",  # ellipsis
            "“": '"',    # left double quotation
            "”": '"',    # right double quotation
            "‘": "'",    # left single quotation
            "’": "'",    # right single quotation
            "–": "-",    # en dash
            "—": "--",   # em dash
            "™": "TM",   # trade mark
            "®": "(R)",  # registered sign
            "©": "(C)",  # copyright
            "☃": "",     # snowman
            "★": "*",    # black star
            "☆": "*",    # white star
            "♥": "<3",   # heart suit
            "☰": "[menu]",    # trigram for heaven (hamburger menu icon)
        }
        for char, repl in replacements.items():
            text = text.replace(char, repl)
        return text.strip()


# ---------------------------------------------------------------------------
# Funcion principal de generacion
# ---------------------------------------------------------------------------
def generate_pdf(md_filename: str, pdf_filename: str,
                 doc_title: str, doc_subtitle: str,
                 meta_lines: list[str]):
    md_path  = os.path.join(DOCS_DIR, md_filename)
    pdf_path = os.path.join(OUTPUT_DIR, pdf_filename)

    print(f"\n{'='*60}")
    print(f"  Generando: {pdf_filename}")
    print(f"{'='*60}")

    with open(md_path, "r", encoding="utf-8") as f:
        content = f.read()

    pdf = SoftGoldPDF(doc_title, doc_subtitle)

    # Portada
    pdf.cover_page(meta_lines)

    # Contenido
    pdf.add_page()
    parser = MarkdownParser(pdf)
    parser.parse(content)

    pdf.output(pdf_path)
    size_kb = os.path.getsize(pdf_path) / 1024
    print(f"  [OK] {pdf_path} ({size_kb:.1f} KB)")


# ---------------------------------------------------------------------------
# Punto de entrada
# ---------------------------------------------------------------------------
if __name__ == "__main__":
    print("\n" + "=" * 60)
    print("  GENERADOR DE DOCUMENTACION SOFTGOLD")
    print("  Sistema de Gestion Integral Minera")
    print("=" * 60)

    generate_pdf(
        md_filename  = "manual_tecnico.md",
        pdf_filename = "SoftGold_Manual_Tecnico.pdf",
        doc_title    = "MANUAL TECNICO DEL SISTEMA",
        doc_subtitle = "Manual Tecnico del Sistema",
        meta_lines   = [
            "Version: 1.0.0",
            "Fecha: Mayo 2026",
            "Dirigido a: Desarrolladores y equipo tecnico",
            "Estado: Documento oficial",
            "",
            "Tecnologias: Spring Boot 3.4.4 | Java 17 | MySQL 8 | Thymeleaf",
            "Puerto: 9090  |  BD: softgold  |  Puerto MySQL: 3306",
        ]
    )

    generate_pdf(
        md_filename  = "manual_usuario.md",
        pdf_filename = "SoftGold_Manual_Usuario.pdf",
        doc_title    = "MANUAL DE USUARIO",
        doc_subtitle = "Manual de Usuario",
        meta_lines   = [
            "Version: 1.0.0",
            "Fecha: Mayo 2026",
            "Dirigido a: Usuarios finales del sistema",
            "Estado: Documento oficial",
            "",
            "Modulos: Minas | Mapas | Riesgos | Exploracion",
            "         Foro | Informes | Soporte | Mi Cuenta",
        ]
    )

    generate_pdf(
        md_filename  = "manual_implementacion.md",
        pdf_filename = "SoftGold_Manual_Implementacion.pdf",
        doc_title    = "MANUAL DE IMPLEMENTACION Y DESPLIEGUE",
        doc_subtitle = "Manual de Implementacion y Despliegue",
        meta_lines   = [
            "Version: 1.0.0",
            "Fecha: Mayo 2026",
            "Dirigido a: Equipo DevOps / Administradores",
            "Estado: Documento oficial",
            "",
            "Ambientes: Desarrollo | QA | Produccion",
            "Infraestructura: Nginx | MySQL | systemd | Docker",
        ]
    )

    generate_pdf(
        md_filename  = "informe_pruebas_rendimiento.md",
        pdf_filename = "SoftGold_Informe_Pruebas_Rendimiento.pdf",
        doc_title    = "INFORME DE PRUEBAS DE RENDIMIENTO",
        doc_subtitle = "Informe de Pruebas de Rendimiento, Carga y Estres",
        meta_lines   = [
            "Version: 1.0.0",
            "Fecha: Mayo 2026",
            "Dirigido a: Equipo QA | DevOps | Arquitectura",
            "Estado: Documento final aprobado",
            "",
            "Pruebas: Carga | Estres | Estabilidad | Concurrencia",
            "Herramientas: JMeter 5.6 | JVisualVM | MySQL EXPLAIN",
        ]
    )

    print("\n" + "=" * 60)
    print("  GENERACION COMPLETADA")
    print(f"  Archivos guardados en: {OUTPUT_DIR}")
    print("=" * 60 + "\n")
