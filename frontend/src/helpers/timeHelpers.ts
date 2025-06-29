export function getNowDatePlusDays(days: number) {
  const nowDate = new Date();
  return new Date(nowDate.getTime() + days * 24 * 60 * 60 * 1000);
}

export function isPast(dateString: string) {
  return new Date().getTime() > new Date(dateString).getTime();
}

export function isFuture(dateString: string) {
  return new Date().getTime() < new Date(dateString).getTime();
}

export function createdAtSortPredicate<T extends { createdAt: string }>(a: T, b: T) {
  return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
}
