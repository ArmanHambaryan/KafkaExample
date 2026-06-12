import { TestBed } from '@angular/core/testing';

import { Kafka } from './kafka';

describe('Kafka', () => {
  let service: Kafka;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Kafka);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
