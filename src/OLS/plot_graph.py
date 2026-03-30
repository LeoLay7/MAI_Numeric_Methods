#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import matplotlib.pyplot as plt
import numpy as np
import os

def read_graph_data(filename):
    """Читает данные из файла для построения графика"""
    original_points = []
    smooth_curves = []
    
    with open(filename, 'r', encoding='utf-8') as file:
        lines = file.readlines()
    
    reading_original = True
    
    for line in lines:
        line = line.strip()
        if line.startswith('#'):
            if 'Расширенные данные' in line:
                reading_original = False
            continue
        
        if not line:
            continue
            
        parts = line.split()
        if len(parts) >= 4:
            x = float(parts[0].replace(',', '.'))
            if reading_original and len(parts) == 5:
                fx = float(parts[1].replace(',', '.'))
                p1x = float(parts[2].replace(',', '.'))
                p2x = float(parts[3].replace(',', '.'))
                p3x = float(parts[4].replace(',', '.'))
                original_points.append([x, fx, p1x, p2x, p3x])
            elif not reading_original and len(parts) == 4:
                p1x = float(parts[1].replace(',', '.'))
                p2x = float(parts[2].replace(',', '.'))
                p3x = float(parts[3].replace(',', '.'))
                smooth_curves.append([x, p1x, p2x, p3x])
    
    return np.array(original_points), np.array(smooth_curves)

def plot_least_squares_approximation():
    """Строит график приближающих многочленов"""
    
    script_dir = os.path.dirname(os.path.abspath(__file__))
    data_file = os.path.join(script_dir, '..', '..', 'resources', 'graph', 'olsres.txt')
    
    if not os.path.exists(data_file):
        print(f"Файл данных не найден: {data_file}")
        print("Сначала запустите Java-программу для генерации данных.")
        return
    
    original_points, smooth_curves = read_graph_data(data_file)
    
    if len(original_points) == 0 or len(smooth_curves) == 0:
        print("Не удалось прочитать данные из файла")
        return
    
    plt.figure(figsize=(12, 8))
    plt.rcParams['font.size'] = 10
    
    x_orig = original_points[:, 0]
    f_orig = original_points[:, 1]
    
    x_smooth = smooth_curves[:, 0]
    p1_smooth = smooth_curves[:, 1]
    p2_smooth = smooth_curves[:, 2]
    p3_smooth = smooth_curves[:, 3]
    
    plt.plot(x_orig, f_orig, 'ko', markersize=8, label='Исходные точки f(x)', zorder=5)
    plt.plot(x_smooth, p1_smooth, 'b-', linewidth=2, label='P₁(x) - линейная аппроксимация')
    plt.plot(x_smooth, p2_smooth, 'g-', linewidth=2, label='P₂(x) - квадратичная аппроксимация')
    plt.plot(x_smooth, p3_smooth, 'r-', linewidth=2, label='P₃(x) - кубическая аппроксимация')
    
    x_star = 5.654
    if len(original_points) > 0:
        idx = np.argmin(np.abs(x_smooth - x_star))
        p1_star = p1_smooth[idx]
        p2_star = p2_smooth[idx]
        p3_star = p3_smooth[idx]
        
        plt.axvline(x=x_star, color='gray', linestyle='--', alpha=0.7, label=f'x* = {x_star}')
        plt.plot(x_star, p1_star, 'bo', markersize=8, zorder=6)
        plt.plot(x_star, p2_star, 'go', markersize=8, zorder=6)
        plt.plot(x_star, p3_star, 'ro', markersize=8, zorder=6)
        
        plt.annotate(f'P₁({x_star}) = {p1_star:.3f}', 
                    xy=(x_star, p1_star), xytext=(x_star+0.5, p1_star+0.5),
                    arrowprops=dict(arrowstyle='->', color='blue', alpha=0.7),
                    fontsize=9, color='blue')
        plt.annotate(f'P₂({x_star}) = {p2_star:.3f}', 
                    xy=(x_star, p2_star), xytext=(x_star+0.5, p2_star+0.5),
                    arrowprops=dict(arrowstyle='->', color='green', alpha=0.7),
                    fontsize=9, color='green')
        plt.annotate(f'P₃({x_star}) = {p3_star:.3f}', 
                    xy=(x_star, p3_star), xytext=(x_star+0.5, p3_star+0.5),
                    arrowprops=dict(arrowstyle='->', color='red', alpha=0.7),
                    fontsize=9, color='red')
    
    plt.xlabel('x', fontsize=12)
    plt.ylabel('y', fontsize=12)
    plt.title('Приближение функции методом наименьших квадратов\nСравнение многочленов различных степеней', 
              fontsize=14, pad=20)
    plt.legend(loc='best', fontsize=10)
    plt.grid(True, alpha=0.3)
    
    plt.tight_layout()
    
    output_file = os.path.join(script_dir, 'least_squares_plot.png')
    plt.savefig(output_file, dpi=300, bbox_inches='tight')
    print(f"График сохранен: {output_file}")
    
    plt.show()
    
    print("\n=== СТАТИСТИКА ===")
    print(f"Количество исходных точек: {len(original_points)}")
    print(f"Количество точек для гладких кривых: {len(smooth_curves)}")
    print(f"Диапазон x: [{x_orig.min():.2f}, {x_orig.max():.2f}]")
    print(f"Диапазон f(x): [{f_orig.min():.2f}, {f_orig.max():.2f}]")

if __name__ == "__main__":
    print("Построение графика приближающих многочленов МНК...")
    plot_least_squares_approximation()
    print("Готово!")