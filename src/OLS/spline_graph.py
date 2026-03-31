# -*- coding: utf-8 -*-
"""
Программа для визуализации естественного кубического сплайна
Показывает, как строится сплайн из отдельных кубических отрезков
"""

import numpy as np
import matplotlib.pyplot as plt

# ============================================================
# 📥 ИСХОДНЫЕ ДАННЫЕ (можно легко заменить на свои)
# ============================================================

# Исходные узлы сплайна
x_nodes = np.array([-5.0, -4.2, -3.1, -2.2, -1.0, 0.3, 1.2, 2.3, 3.5, 4.2, 5.0])
y_nodes = np.array([3.679, 2.982, 1.987, 2.184, 1.056, 1.342, 0.735, 1.169, 1.132, 1.576, 2.543])

# Точка, в которой вычисляли значение сплайна
x_star = -0.224

# Коэффициенты сплайна для каждого отрезка [x_i, x_{i+1}]
# Формат: [a_i, b_i, c_i, d_i] для формулы: S(x) = a + b*t + c*t² + d*t³, где t = x - x_i
coefficients = np.array([
    [3.679000, -0.764629,  0.000000, -0.166596],  # [-5.0, -4.2]
    [2.982000, -1.084493, -0.399831,  0.512199],  # [-4.2, -3.1]
    [1.987000, -0.104837,  1.290427, -1.034147],  # [-3.1, -2.2]
    [2.184000, -0.295044, -1.501769,  0.803588],  # [-2.2, -1.0]
    [1.056000, -0.427789,  1.391148, -0.686807],  # [-1.0,  0.3] ← здесь лежит x*
    [1.342000, -0.292915, -1.287399,  0.959419],  # [ 0.3,  1.2]
    [0.735000, -0.278845,  1.303032, -0.628054],  # [ 1.2,  2.3]
    [1.169000,  0.307991, -0.769545,  0.405994],  # [ 2.3,  3.5]
    [1.132000,  0.214973,  0.692031, -0.132877],  # [ 3.5,  4.2]
    [1.576000,  0.988488,  0.412990, -0.172079],  # [ 4.2,  5.0]
])

# ============================================================
# ⚙️ ФУНКЦИИ ДЛЯ РАБОТЫ СО СПЛАЙНОМ
# ============================================================

def evaluate_spline_segment(a, b, c, d, t):
    """
    Вычисляет значение кубического многочлена на отрезке.

    Параметры:
        a, b, c, d — коэффициенты сплайна
        t — смещение от левого конца отрезка: t = x - x_i

    Возвращает:
        S(x) = a + b·t + c·t² + d·t³
    """
    return a + b * t + c * t**2 + d * t**3


def find_segment_index(x, x_nodes):
    """
    Находит индекс отрезка [x_i, x_{i+1}], в котором лежит точка x.

    Возвращает:
        i — индекс левого узла отрезка, или None если x вне диапазона
    """
    for i in range(len(x_nodes) - 1):
        if x_nodes[i] <= x <= x_nodes[i+1]:
            return i
    return None


def spline_value(x, x_nodes, coefficients):
    """
    Вычисляет значение сплайна в произвольной точке x.
    """
    i = find_segment_index(x, x_nodes)
    if i is None:
        raise ValueError(f"Точка x={x} вне диапазона сплайна [{x_nodes[0]}, {x_nodes[-1]}]")

    a, b, c, d = coefficients[i]
    t = x - x_nodes[i]  # смещение от левого конца
    return evaluate_spline_segment(a, b, c, d, t)


# ============================================================
# 🎨 ПОСТРОЕНИЕ ГРАФИКА
# ============================================================

def plot_spline(x_nodes, y_nodes, coefficients, x_star=None):
    """
    Строит наглядный график кубического сплайна.
    """
    # Создаём фигуру и оси
    fig, ax = plt.subplots(figsize=(12, 7))

    # Цвета для разных отрезков (чтобы видеть "кусочки" сплайна)
    colors = plt.cm.viridis(np.linspace(0, 1, len(coefficients)))

    # 📐 Строим каждый отрезок сплайна отдельно
    for i, (a, b, c, d) in enumerate(coefficients):
        x_left = x_nodes[i]
        x_right = x_nodes[i + 1]

        # Генерируем 50 точек для плавной кривой на этом отрезке
        t_values = np.linspace(0, x_right - x_left, 50)
        x_values = x_left + t_values
        y_values = evaluate_spline_segment(a, b, c, d, t_values)

        # Рисуем отрезок сплайна
        ax.plot(x_values, y_values, color=colors[i], linewidth=2.5,
                label=f'[{x_left}, {x_right}]' if i < 5 else "")

    # 🔵 Рисуем исходные узлы (точки, через которые проходит сплайн)
    ax.scatter(x_nodes, y_nodes, color='blue', s=80, zorder=5,
               edgecolors='white', linewidth=1.5, label='Исходные узлы')

    # 🔴 Если задана точка x* — отмечаем её на графике
    if x_star is not None:
        y_star = spline_value(x_star, x_nodes, coefficients)
        ax.scatter([x_star], [y_star], color='red', s=120, zorder=6,
                   edgecolors='white', linewidth=2, marker='*',
                   label=f'x* = {x_star}, S(x*) = {y_star:.5f}')

        # Пунктирная линия до осей для наглядности
        ax.axvline(x=x_star, color='red', linestyle='--', alpha=0.4)
        ax.axhline(y=y_star, color='red', linestyle='--', alpha=0.4)

    # 🎨 Оформление графика
    ax.set_xlabel('x', fontsize=12)
    ax.set_ylabel('y = S(x)', fontsize=12)
    ax.set_title('🎢 Естественный кубический сплайн (дефекта 1)', fontsize=14, pad=20)
    ax.grid(True, alpha=0.3, linestyle=':')
    ax.legend(loc='best', fontsize=9, framealpha=0.9)

    # Добавляем подписи значений в узлах (опционально, для наглядности)
    for x, y in zip(x_nodes, y_nodes):
        ax.annotate(f'{y:.2f}', xy=(x, y), xytext=(0, 8),
                    textcoords='offset points', fontsize=7,
                    ha='center', alpha=0.7)

    # Убираем лишние отступы
    plt.tight_layout()

    # Показываем график
    plt.show()

    return fig, ax


# ============================================================
# 🚀 ЗАПУСК ПРОГРАММЫ
# ============================================================

if __name__ == "__main__":
    print("=== 🎢 Построение кубического сплайна ===")
    print(f"Узлы: {len(x_nodes)} точек от {x_nodes[0]} до {x_nodes[-1]}")
    print(f"Отрезков: {len(coefficients)}")
    print(f"Точка вычисления: x* = {x_star}")

    # Вычисляем и выводим значение в x*
    y_star = spline_value(x_star, x_nodes, coefficients)
    print(f"✅ S({x_star}) = {y_star:.6f}")

    # 🔥 Строим график!
    print("\n📊 Открываю график...")
    plot_spline(x_nodes, y_nodes, coefficients, x_star)

    print("✅ Готово! Закройте окно графика для завершения программы.")