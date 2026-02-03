import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat',
  standalone: true
})
export class DateFormatPipe implements PipeTransform {

  transform(date: Date | null | undefined): string {
    if (!date) {
      return '';
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const inputDate = new Date(date);
    inputDate.setHours(0, 0, 0, 0);

    const daysDiff = Math.floor((inputDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

    if (this.needFormat(daysDiff)) {
      return this.formatDate(daysDiff, inputDate);
    } else {
      return this.formatMonth(inputDate);
    }
  }

  private needFormat(daysDiff: number): boolean {
    return daysDiff > -8;
  }

  private formatDate(daysDiff: number, date: Date): string {
    if (daysDiff === 0) return '今日';
    if (daysDiff === -1) return '昨日';
    if (daysDiff === 1) return '明日';
    if (daysDiff > 0) return `あと${daysDiff}日`;
    if (daysDiff > -8) return `${-daysDiff}日前`;

    return this.formatShortDate(date);
  }

  private formatShortDate(date: Date): string {
    const year = date.getFullYear().toString().slice(-2);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private formatMonth(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    return `${year}-${month}`;
  }
}
